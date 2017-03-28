package de.florianschmitt.rest

import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.model.rest.ELanguageDTO
import de.florianschmitt.rest.util.HasRequesterRole
import de.florianschmitt.service.LanguageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@HasRequesterRole
internal class LanguageController {

    @Autowired
    private lateinit var service: LanguageService

    @RequestMapping(path = arrayOf("/getLanguages"), method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    fun getLanguages(@RequestParam(name = "locale", defaultValue = "de") locale: String): ResponseEntity<List<ELanguageDTO>> {
        val language = ELocalizedLanguageEnum.create(locale)
        val result = service.findByLanguage(language)
        return ResponseEntity.ok(result)
    }
}
