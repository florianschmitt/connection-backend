package de.florianschmitt.rest

import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.model.rest.ELanguageDTO
import de.florianschmitt.service.ENGLISH_LANGUAGE_IDENTIFER
import de.florianschmitt.service.LanguageService
import de.florianschmitt.system.util.HasRequesterRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@HasRequesterRole
internal class LanguageController {

    @Autowired
    private lateinit var service: LanguageService

    @GetMapping(path = ["/getLanguages"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getLanguages(@RequestParam(name = "locale", defaultValue = "de") locale: String): ResponseEntity<List<ELanguageDTO>> {
        val language = ELocalizedLanguageEnum.create(locale)
        val result = service.findByLanguageFilterEnglish(language)
        return ResponseEntity.ok(result)
    }

    @GetMapping(path = ["/getEnglishLanguage"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getEnglishLanguage(@RequestParam(name = "locale", defaultValue = "de") locale: String): ResponseEntity<Any> {
        val language = ELocalizedLanguageEnum.create(locale)
        val result = service.findByIdentifier(language, ENGLISH_LANGUAGE_IDENTIFER)
        if (result.isPresent) {
            return ResponseEntity.ok(result.get())
        }
        return ResponseEntity.notFound().build()
    }
}
