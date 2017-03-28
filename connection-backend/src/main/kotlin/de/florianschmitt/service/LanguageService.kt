package de.florianschmitt.service

import de.florianschmitt.model.entities.ELanguage
import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.model.rest.ELanguageDTO
import de.florianschmitt.repository.LanguageRepository
import org.springframework.stereotype.Service

@Service
class LanguageService : AbstractPageableAdminService<ELanguage, LanguageRepository>() {

    fun hasAtLeastOneLanguage() = repository.count() > 0L

    fun findByLanguage(language: ELocalizedLanguageEnum): List<ELanguageDTO> {
        return repository.findAllByOrderByViewOrder()
                .associate { it.id to it.valueInLanguage(language) }
                .map { ELanguageDTO(it.key, it.value) }
                .toList()
    }
}
