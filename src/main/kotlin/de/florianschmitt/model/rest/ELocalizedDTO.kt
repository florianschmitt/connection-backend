package de.florianschmitt.model.rest

import javax.validation.constraints.NotBlank

import java.io.Serializable

class ELocalizedDTO : Serializable {

    var id: Long? = null

    @NotBlank
    var value: String? = null

    @NotBlank
    var locale: String? = null
}
