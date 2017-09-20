package de.florianschmitt.model.entities

import org.hibernate.validator.constraints.NotBlank

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class ELanguage : BaseEntity {

    constructor(identifier: String, viewOrder: Int) {
        this.identifier = identifier
        this.viewOrder = viewOrder
    }

    @Column(nullable = false, unique = true)
    @NotBlank
    var identifier: String

    @Column(nullable = false)
    var viewOrder: Int = 0

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
