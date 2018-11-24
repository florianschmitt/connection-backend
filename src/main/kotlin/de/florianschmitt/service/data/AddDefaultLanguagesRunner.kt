package de.florianschmitt.service.data

import de.florianschmitt.model.entities.ELanguage
import de.florianschmitt.model.entities.ELocalized
import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import de.florianschmitt.service.LanguageService
import de.florianschmitt.system.util.DevPostgresProfile
import de.florianschmitt.system.util.DevProfile
import de.florianschmitt.system.util.ProductiveProfile
import de.florianschmitt.system.util.TestingProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@ProductiveProfile
@TestingProfile
@DevPostgresProfile
@DevProfile
@Component
class AddDefaultLanguagesRunner : CommandLineRunner {

    @Autowired
    private lateinit var languageService: LanguageService

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        addLanguages()
    }

    private fun addLanguages() {
        if (languageService.hasAtLeastOneLanguage())
            return

        addArabic()
        addTurkish()
        addSerboCroatic()
        addEnglish()
    }

    private fun addArabic() {
        val l = ELanguage("ARABIC", 0)

        val l1 = ELocalized(l, ELocalizedLanguageEnum.DE, "Arabisch")
        val l2 = ELocalized(l, ELocalizedLanguageEnum.AR, "العربية")
        val l3 = ELocalized(l, ELocalizedLanguageEnum.EN, "Arabic")

        l.localized = setOf(l1, l2, l3)
        languageService.save(l)
    }

    private fun addTurkish() {
        val l = ELanguage("TURKISH", 1)

        val l1 = ELocalized(l, ELocalizedLanguageEnum.DE, "Türkisch")
        val l2 = ELocalized(l, ELocalizedLanguageEnum.AR, "اللغة التركية")
        val l3 = ELocalized(l, ELocalizedLanguageEnum.EN, "Turkish")

        l.localized = setOf(l1, l2, l3)
        languageService.save(l)
    }

    private fun addSerboCroatic() {
        val l = ELanguage("SERBOCROATIAN", 2)

        val l1 = ELocalized(l, ELocalizedLanguageEnum.DE, "Serbo-Kroatisch")
        val l2 = ELocalized(l, ELocalizedLanguageEnum.AR, "اللغة التركية")
        val l3 = ELocalized(l, ELocalizedLanguageEnum.EN, "Serbo-Croatian")

        l.localized = setOf(l1, l2, l3)
        languageService.save(l)
    }

    private fun addEnglish() {
        val l = ELanguage("ENGLISH", 5)

        val l1 = ELocalized(l, ELocalizedLanguageEnum.DE, "Englisch")
        val l2 = ELocalized(l, ELocalizedLanguageEnum.AR, "العربية")
        val l3 = ELocalized(l, ELocalizedLanguageEnum.EN, "English")

        l.localized = setOf(l1, l2, l3)
        languageService.save(l)
    }

}
