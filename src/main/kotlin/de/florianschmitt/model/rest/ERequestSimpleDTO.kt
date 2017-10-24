package de.florianschmitt.model.rest

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import java.io.Serializable
import java.time.LocalDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class ERequestSimpleDTO : Serializable {

    @NotNull
    @Size(min = 1)
    var languageIds: Set<Long>? = null

    @NotNull
    var datetime: LocalDateTime? = null

    @NotBlank
    var ocation: String? = null

    @NotBlank
    var street: String? = null

    @NotBlank
    var postalCode: String? = null

    @NotBlank
    var city: String? = null
}