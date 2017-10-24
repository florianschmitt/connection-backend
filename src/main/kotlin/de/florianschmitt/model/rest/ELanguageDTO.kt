package de.florianschmitt.model.rest

import javax.validation.constraints.NotBlank

import java.io.Serializable

class ELanguageDTO(id: Long, label: String) : Serializable {

    var id: Long? = id

    @NotBlank
    var label: String? = label
}
