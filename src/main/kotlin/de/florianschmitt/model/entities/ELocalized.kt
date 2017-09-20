package de.florianschmitt.model.entities

import de.florianschmitt.model.entities.util.LocalizedLanguageEnumConverter
import org.hibernate.validator.constraints.NotBlank

import javax.persistence.*

@Entity
class ELocalized : BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    var language: ELanguage

    @Column(nullable = false)
    @Convert(converter = LocalizedLanguageEnumConverter::class)
    var localeLanguage: ELocalizedLanguageEnum

    @Column(nullable = false)
    @NotBlank
    var value: String

    constructor(language: ELanguage, localeLanguage: ELocalizedLanguageEnum, value: String) {
        this.language = language
        this.localeLanguage = localeLanguage
        this.value = value
    }
}
