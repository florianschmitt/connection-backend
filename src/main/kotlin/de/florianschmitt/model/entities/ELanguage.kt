package de.florianschmitt.model.entities

import javax.validation.constraints.NotBlank

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class ELanguage(identifier: String, viewOrder: Int) : BaseEntity() {

    @Column(nullable = false, unique = true)
    @NotBlank
    var identifier: String = identifier

    @Column(nullable = false)
    var viewOrder: Int = viewOrder

    @OneToMany(mappedBy = "language", cascade = arrayOf(CascadeType.MERGE, CascadeType.PERSIST))
    var localized: Set<ELocalized>? = null

    val valueInDefaultLanguage: String
        get() {
            val defaultLang = ELocalizedLanguageEnum.DE
            return valueInLanguage(defaultLang);
        }

    fun valueInLanguage(lang: ELocalizedLanguageEnum) = localized
            ?.find { it -> it.localeLanguage == lang }
            ?.let(ELocalized::value) ?: identifier
}
