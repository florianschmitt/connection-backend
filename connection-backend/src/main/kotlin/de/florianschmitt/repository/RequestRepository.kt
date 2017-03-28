package de.florianschmitt.repository

import java.util.Optional

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ERequestStateEnum

interface RequestRepository : PagingAndSortingRepository<ERequest, Long> {
    fun countByState(state: ERequestStateEnum): Long

    fun findByRequestIdentifier(requestIdentifier: String): Optional<ERequest>

    @EntityGraph(attributePaths = arrayOf("languages"))
    fun findByRequestIdentifier_(requestIdentifier: String): Optional<ERequest>

    @Query(value = "Select r From ERequest r Where r.payments Is Empty", countQuery = "Select Count(r) From ERequest r Where r.payments Is Empty") //
    fun findAllNotPayed(pageable: Pageable): Page<ERequest>

}
