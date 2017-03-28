package de.florianschmitt.repository

import de.florianschmitt.model.entities.ETemplate
import org.springframework.data.repository.CrudRepository

import java.util.Optional

interface TemplateRepository : CrudRepository<ETemplate, Long> {
    fun findByIdentifier(identifier: String): Optional<ETemplate>
}
