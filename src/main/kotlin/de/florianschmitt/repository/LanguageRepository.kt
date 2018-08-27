package de.florianschmitt.repository

import org.springframework.data.repository.PagingAndSortingRepository

import de.florianschmitt.model.entities.ELanguage
import java.util.*

interface LanguageRepository : PagingAndSortingRepository<ELanguage, Long> {
    fun findAllByOrderByViewOrder(): List<ELanguage>

    fun findByIdentifier(identifier: String): Optional<ELanguage>
}
