package de.florianschmitt.service

import de.florianschmitt.model.entities.ELanguage
import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.model.rest.ELanguageDTO
import de.florianschmitt.repository.LanguageRepository
import de.florianschmitt.service.base.AbstractPageableAdminService
import org.springframework.stereotype.Service

const val ENGLISH_LANGUAGE_IDENTIFER = "ENGLISH"

@Service
class LanguageService : AbstractPageableAdminService<ELanguage, LanguageRepository>() {

    fun hasAtLeastOneLanguage() = repository.count() > 0L

    fun findByLanguage(language: ELocalizedLanguageEnum): List<ELanguageDTO> {
        return repository.findAllByOrderByViewOrder()
                .associate { it.id to it.valueInLanguage(language) }
                .map { ELanguageDTO(it.key, it.value) }
                .toList()
    }

    fun findByLanguageFilterEnglish(language: ELocalizedLanguageEnum): List<ELanguageDTO> {
        return repository.findAllByOrderByViewOrder()
                .filter { it.identifier != ENGLISH_LANGUAGE_IDENTIFER }
                .associate { it.id to it.valueInLanguage(language) }
                .map { ELanguageDTO(it.key, it.value) }
                .toList()
    }

    fun findByIdentifier(language: ELocalizedLanguageEnum, identifier: String) = repository.findByIdentifier(identifier)
            .map { ELanguageDTO(it.id, it.valueInLanguage(language)) }
}
