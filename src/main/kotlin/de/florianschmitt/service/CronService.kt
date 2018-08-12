package de.florianschmitt.service;

import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CronService {

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    fun closeRequestsWhichAreFinished() {
        val start = LocalDateTime.now()
        val end = start.plusDays(1)

        log.trace("closeRequestsWhichAreFinished")

        val dueRequests = requestRepository.findOpenRequestsWhichAreDue(start, end)
        for (request in dueRequests) {
            when (request.state) {
                ERequestStateEnum.OPEN -> {
                    request.state = ERequestStateEnum.EXPIRED
                    requestRepository.save(request)
                }
                ERequestStateEnum.ACCEPTED -> {
                    request.state = ERequestStateEnum.FINISHED
                    requestRepository.save(request)
                }
                else -> {
                }
            }
        }
    }
}
