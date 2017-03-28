package de.florianschmitt.model.entities


enum class ELocalizedLanguageEnum {
    DE, EN, AR;

    companion object {
        fun create(locale: String): ELocalizedLanguageEnum {
            return valueOf(locale.toUpperCase())
        }
    }
}
