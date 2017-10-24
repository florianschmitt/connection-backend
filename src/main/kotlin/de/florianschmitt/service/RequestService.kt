package de.florianschmitt.service

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.model.entities.EVoucher
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.repository.VolunteerRepository
import de.florianschmitt.repository.VoucherRepository
import de.florianschmitt.rest.exception.*
import de.florianschmitt.service.util.TransactionHook
import de.florianschmitt.system.generators.IdentifierGenerator
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.RollbackException

@Service
class RequestService {
    @Autowired
    private lateinit var repository: RequestRepository

    @Autowired
    private lateinit var voucherRepository: VoucherRepository

    @Autowired
    private lateinit var volunteerRepository: VolunteerRepository

    @Autowired
    private lateinit var mailService: MailService

    @Autowired
    private lateinit var identifierGenerator: IdentifierGenerator

    fun all(pageable: Pageable): Page<ERequest> {
        return repository.findAll(pageable)
    }

    fun findAllNotPayed(pageable: Pageable): Page<ERequest> {
        return repository.findAllNotPayed(pageable)
    }

    @Transactional
    fun declineRequest(requestIdentifier: String) {
        val request = findByRequestIdentifier(requestIdentifier)

        checkCanceledFinishedStateOrFail(request)

        if (request.state == ERequestStateEnum.ACCEPTED) {
            // val acceptedByVolunteer = request.acceptedByVolunteer
            // TODO: inform volunteer
        }

        request.state = ERequestStateEnum.CANCELED
        repository.save(request)
    }

    fun findByRequestIdentifier(requestIdentifier: String): ERequest {
        val request = repository.findByRequestIdentifier(requestIdentifier)
                .orElseThrow({ RequestNotFoundException() })
        return request
    }

    fun findByVoucherIdentifier(voucherIdentifier: String): ERequest {
        val voucher = voucherRepository.findByIdentifier(voucherIdentifier)
                .orElseThrow({ VoucherNotFoundException() })
        return voucher.request
    }

    fun listVouchers(requestIdentifier: String): Collection<EVoucher> {
        val request = findByRequestIdentifier(requestIdentifier)
        val vouchers = voucherRepository.findByRequest(request)
        return vouchers
    }

    @Transactional
    fun answerRequest(voucherIdentifier: String, answer: Boolean) {
        val voucher = voucherRepository.findByIdentifier(voucherIdentifier)
                .orElseThrow({ VoucherNotFoundException() })

        if (voucher.answer != null) {
            throw VoucherAlreadyAnsweredException()
        }

        if (!answer) {
            voucher.answer = false
            voucherRepository.save(voucher)
            return
        }

        val request = voucher.request

        checkAcceptedStateOrFail(request)
        checkCanceledFinishedStateOrFail(request)

        voucher.answer = true
        voucherRepository.save(voucher)

        request.acceptedByVolunteer = voucher.volunteer
        request.state = ERequestStateEnum.ACCEPTED
        repository.save(request)

        TransactionHook.afterCommitSuccess {
            mailService.requestAcceptedConfirmationForVolunteer(request)
        }

        TransactionHook.afterCommitSuccess {
            mailService.requestAcceptedConfirmationForRequester(request)
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    fun closeRequestsWhichAreFinished() {
        val start = LocalDateTime.now()
        val end = start.plusDays(1)

        log.trace("closeRequestsWhichAreFinished")

        val dueRequests = repository.findOpenRequestsWhichAreDue(start, end)
        for (request in dueRequests) {
            when (request.state) {
                ERequestStateEnum.OPEN -> {
                    request.state = ERequestStateEnum.EXPIRED
                    repository.save(request)
                }
                ERequestStateEnum.ACCEPTED -> {
                    request.state = ERequestStateEnum.FINISHED
                    repository.save(request)
                }
                else -> {
                }
            }
        }
    }

    fun checkVoucherValid(voucherIdentifier: String) {
        val voucher = voucherRepository.findByIdentifier(voucherIdentifier)
                .orElseThrow({ VoucherNotFoundException() })

        if (voucher.answer != null) {
            throw VoucherAlreadyAnsweredException()
        }

        val request = voucher.request

        checkAcceptedStateOrFail(request)
        checkCanceledFinishedStateOrFail(request)
    }

    private fun checkCanceledFinishedStateOrFail(request: ERequest) {
        when (request.state) {
            ERequestStateEnum.CANCELED -> throw RequestWasCanceledException()
            ERequestStateEnum.FINISHED -> throw RequestFinishedException()
            else -> {
            }
        }
    }

    private fun checkAcceptedStateOrFail(request: ERequest) {
        if (request.state == ERequestStateEnum.ACCEPTED) {
            throw RequestAlreadyAcceptedException()
        }
    }

    @Transactional
    fun submitNewRequest(givenRequest: ERequest): String {
        var request = givenRequest
        if (!request.isNew) {
            throw RollbackException("only accepts new requests")
        }

        request.state = ERequestStateEnum.OPEN
        request.requestIdentifier = identifierGenerator.generate()
        request.createdAt = LocalDateTime.now()

        request = repository.save(request)
        val vouchers = createVouchers(request)
//        if (vouchers.isEmpty()) throw RuntimeException("what to do if no vouchers were created?")
        val result = request.requestIdentifier ?: throw RuntimeException("requestIdentifier cannot be null")

        TransactionHook.afterCommitSuccess {
            vouchers.forEach {
                mailService.requestAskVolunteer(request, it)
            }
        }

        return result
    }

    private fun createVouchers(request: ERequest): Collection<EVoucher> {
        val result = volunteerRepository.findActiveVolunteerWhoHasLanguage(request.languages!!)
                .map { volunteer -> EVoucher(identifierGenerator.generate(), volunteer, request) }
                .map(voucherRepository::save)
                .toHashSet()
        return result
    }
}
