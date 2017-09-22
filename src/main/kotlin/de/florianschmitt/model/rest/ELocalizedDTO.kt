package de.florianschmitt.model.rest

import org.hibernate.validator.constraints.NotBlank

import java.io.Serializable

class ELocalizedDTO : Serializable {

    var id: Long? = null

    @NotBlank
    var value: String? = null

    @NotBlank
    var locale: String? = null
}
