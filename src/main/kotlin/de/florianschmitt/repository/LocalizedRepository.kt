package de.florianschmitt.repository

import org.springframework.data.repository.CrudRepository

import de.florianschmitt.model.entities.ELocalized

interface LocalizedRepository : CrudRepository<ELocalized, Long> {
    fun findByLocaleLanguage(localeLanguage: String): Set<ELocalized>
}
