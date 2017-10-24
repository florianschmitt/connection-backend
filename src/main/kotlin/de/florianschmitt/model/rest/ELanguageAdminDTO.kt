package de.florianschmitt.model.rest

import javax.validation.constraints.NotBlank

import java.io.Serializable

class ELanguageAdminDTO : Serializable {

    var id: Long? = null

    @NotBlank
    var identifier: String? = null

    var viewOrder: Int = 0

    var localized: List<ELocalizedDTO>? = null
}
