package de.florianschmitt.model.entities

import de.florianschmitt.model.entities.util.LocalizedLanguageEnumConverter
import javax.validation.constraints.NotBlank

import javax.persistence.*

@Entity
class ELocalized(language: ELanguage, localeLanguage: ELocalizedLanguageEnum, value: String) : BaseEntity() {

    @ManyToOne
    @JoinColumn(nullable = false)
    var language: ELanguage = language

    @Column(nullable = false)
    @Convert(converter = LocalizedLanguageEnumConverter::class)
    var localeLanguage: ELocalizedLanguageEnum = localeLanguage

    @Column(nullable = false)
    @NotBlank
    var value: String = value
}
