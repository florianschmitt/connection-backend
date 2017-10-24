package de.florianschmitt.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.model.rest.EDashboardDTO
import de.florianschmitt.repository.RequestRepository

@Service
class DashboardService {

    @Autowired
    lateinit var repository: RequestRepository

    fun info(): EDashboardDTO {
        val open = repository.countByState(ERequestStateEnum.OPEN)
        val accepted = repository.countByState(ERequestStateEnum.ACCEPTED)
        val canceled = repository.countByState(ERequestStateEnum.CANCELED)
        val finished = repository.countByState(ERequestStateEnum.FINISHED)
        val expired = repository.countByState(ERequestStateEnum.EXPIRED)
        return EDashboardDTO(open, accepted, canceled, finished, expired)
    }
}
