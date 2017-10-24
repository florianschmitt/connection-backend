package de.florianschmitt.model.entities.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateConverter {
    fun convertStandard(date: LocalDateTime?) = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm").format(date)
}