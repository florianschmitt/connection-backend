package de.florianschmitt.repository

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ERequestStateEnum
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface RequestRepository : PagingAndSortingRepository<ERequest, Long> {
    fun countByState(state: ERequestStateEnum): Long

    fun findByRequestIdentifier(requestIdentifier: String): Optional<ERequest>

    @EntityGraph(attributePaths = arrayOf("languages"))
    fun findByRequestIdentifier_(requestIdentifier: String): Optional<ERequest>

    @Query(value = "Select r From ERequest r Where r.payments Is Empty",
            countQuery = "Select Count(r) From ERequest r Where r.payments Is Empty")
    fun findAllNotPayed(pageable: Pageable): Page<ERequest>

    @Query(value = "Select r From ERequest r Where r.state = 'OPEN' and r.datetime between :startDate and :endDate")
    fun findOpenRequestsWhichAreDue(@Param("startDate") startDate: LocalDateTime, @Param("endDate") endDate: LocalDateTime): Collection<ERequest>

}
