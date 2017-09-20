package de.florianschmitt.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

import de.florianschmitt.model.entities.EPayment
import org.springframework.data.repository.PagingAndSortingRepository

interface PaymentRepository : PagingAndSortingRepository<EPayment, Long> {
    fun findByRequest_RequestIdentifier(requestIdentifier: String, request: Pageable): Page<EPayment>
}
