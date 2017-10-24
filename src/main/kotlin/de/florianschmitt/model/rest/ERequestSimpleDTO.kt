package de.florianschmitt.model.rest

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
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