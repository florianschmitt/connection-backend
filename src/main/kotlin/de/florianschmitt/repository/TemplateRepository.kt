package de.florianschmitt.repository

import de.florianschmitt.model.entities.ETemplate
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

import java.util.Optional

interface TemplateRepository : PagingAndSortingRepository<ETemplate, Long> {
    fun findByIdentifier(identifier: String): Optional<ETemplate>
}
