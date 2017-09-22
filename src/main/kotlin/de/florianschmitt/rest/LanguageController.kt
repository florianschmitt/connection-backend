package de.florianschmitt.rest

import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.model.rest.ELanguageDTO
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

    @GetMapping(path = arrayOf("/getLanguages"), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getLanguages(@RequestParam(name = "locale", defaultValue = "de") locale: String): ResponseEntity<List<ELanguageDTO>> {
        val language = ELocalizedLanguageEnum.create(locale)
        val result = service.findByLanguage(language)
        return ResponseEntity.ok(result)
    }
}
