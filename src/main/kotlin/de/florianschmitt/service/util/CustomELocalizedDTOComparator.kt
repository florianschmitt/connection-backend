package de.florianschmitt.service.util

import de.florianschmitt.model.rest.ELocalizedDTO
import java.util.*

class CustomELocalizedDTOComparator : Comparator<ELocalizedDTO> {

    override fun compare(left: ELocalizedDTO, right: ELocalizedDTO): Int {
        val leftLocale = left.locale
        val rightLocale = right.locale

        val comparator = Comparator.comparingInt<String> { localeOrder(it) }
        return comparator.compare(leftLocale, rightLocale)
    }

    companion object {
        val INSTANCE = CustomELocalizedDTOComparator()

        fun localeOrder(locale: String) = when (locale) {
            "de" -> 0
            "en" -> 1
            "ar" -> 2
            else -> 3
        }
    }
}
