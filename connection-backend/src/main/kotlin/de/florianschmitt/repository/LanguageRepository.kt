package de.florianschmitt.repository

import org.springframework.data.repository.PagingAndSortingRepository

import de.florianschmitt.model.entities.ELanguage

interface LanguageRepository : PagingAndSortingRepository<ELanguage, Long> {
    fun findAllByOrderByViewOrder(): List<ELanguage>
}
