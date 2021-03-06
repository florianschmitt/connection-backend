package de.florianschmitt.service

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.model.entities.EVoucher
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.repository.VolunteerRepository
import de.florianschmitt.repository.VoucherRepository
import de.florianschmitt.rest.exception.*
import de.florianschmitt.service.events.*
import de.florianschmitt.service.util.TransactionHook
import de.florianschmitt.system.generators.IdentifierGenerator
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    private lateinit var identifierGenerator: IdentifierGenerator

    @Autowired
    private lateinit var context: ApplicationContext

    fun all(pageable: Pageable): Page<ERequest> {
        return repository.findAll(pageable)
    }

    fun findAllNotPayed(pageable: Pageable): Page<ERequest> {
        return repository.findAllNotPayed(pageable)
    }

    @Transactional
    fun expireRequest(request: ERequest) {
        request.state = ERequestStateEnum.EXPIRED
        val requestSaved = repository.save(request)

        log.info("$requestSaved was expired")

        TransactionHook.afterCommitSuccess {
            context.publishEvent(RequestIsExpiredEvent(this@RequestService, requestSaved))
        }
    }

    @Transactional
    fun finishRequest(request: ERequest) {
        request.state = ERequestStateEnum.FINISHED
        val requestSaved = repository.save(request)

        log.info("$requestSaved was finished")

        TransactionHook.afterCommitSuccess {
            context.publishEvent(RequestIsFinishedEvent(this@RequestService, requestSaved))
        }
    }

    @Transactional
    fun declineRequest(requestIdentifier: String) {
        val request = findByRequestIdentifier(requestIdentifier)

        checkCanceledFinishedStateOrFail(request)

        val previousState = request.state!!
        request.state = ERequestStateEnum.CANCELED
        val requestSaved = repository.save(request)

        TransactionHook.afterCommitSuccess {
            context.publishEvent(RequestWasCanceledEvent(this@RequestService, requestSaved, previousState))
        }
    }

    fun findByRequestIdentifier(requestIdentifier: String): ERequest {
        val request = repository.findByRequestIdentifier(requestIdentifier)
                .orElseThrow { RequestNotFoundException() }
        return request
    }

    fun findByVoucherIdentifier(voucherIdentifier: String): ERequest {
        val voucher = voucherRepository.findByIdentifier(voucherIdentifier)
                .orElseThrow { VoucherNotFoundException() }
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
                .orElseThrow { VoucherNotFoundException() }

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
        val requestSaved = repository.save(request)

        TransactionHook.afterCommitSuccess {
            context.publishEvent(RequestWasAcceptedEvent(this@RequestService, requestSaved))
        }
    }

    fun checkVoucherValid(voucherIdentifier: String) {
        val voucher = voucherRepository.findByIdentifier(voucherIdentifier)
                .orElseThrow { VoucherNotFoundException() }

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

        if (request.datetime == null && request.dateDescription == null) {
            throw RollbackException("either datetime or dateDescription must be set")
        }

        request.state = ERequestStateEnum.OPEN
        request.requestIdentifier = identifierGenerator.generate()
        request.createdAt = LocalDateTime.now()

        val requestSaved = repository.save(request)
        val vouchers = createVouchers(requestSaved)
//        if (vouchers.isEmpty()) throw RuntimeException("what to do if no vouchers were created?")
        val result = requestSaved.requestIdentifier ?: throw RuntimeException("requestIdentifier cannot be null")

        TransactionHook.afterCommitSuccess {
            context.publishEvent(RequestWasSubmittedEvent(this@RequestService, requestSaved, vouchers))
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
