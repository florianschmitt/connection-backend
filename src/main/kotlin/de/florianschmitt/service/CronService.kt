package de.florianschmitt.service;

import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.system.util.NotTestProfile
import de.florianschmitt.system.util.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@NotTestProfile
class CronService {

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Autowired
    private lateinit var requestService: RequestService

    @Scheduled(cron = "0/15 * * * * *") // jede Minute
//    @Scheduled(cron = "0 0 20 * * *") // jeden Tag um 20 Uhr
    fun closeRequestsWhichAreFinished() {
        checkForFinished()
        checkForExpired()
    }

    fun checkForFinished() {
        val start = LocalDateTime.now()
        val end = start.plusDays(1)

        log.info("checkForFinished $start - $end")

        requestRepository.findAcceptedRequestsWhichAreDue(start, end)
                .forEach(requestService::finishRequest)
    }

    fun checkForExpired() {
        val start = LocalDateTime.now()
        val end = start.plusDays(1)

        log.info("checkForExpired $start - $end")

        requestRepository.findOpenRequestsWhichAreDue(start, end)
                .stream()
                .forEach(requestService::expireRequest)
    }
}
