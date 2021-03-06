package de.florianschmitt.model.rest

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column

class ERequestDTO : Serializable {

    var requestIdentifier: String? = null

    @NotNull
    @Size(min = 1)
    var languageIds: Set<Long>? = null

    var datetime: LocalDateTime? = null

    var dateDescription: String? = null

    @NotBlank
    var occasionEnum: String? = null

    var occasionString: String? = null

    @NotBlank
    var street: String? = null

    @NotBlank
    var postalCode: String? = null

    @NotBlank
    var city: String? = null

    @NotBlank
    var requesterName: String? = null

    var requesterInstitution: String? = null

    @NotBlank
    @Email
    var email: String? = null

    @NotBlank
    var phone: String? = null

    var state: String? = null

    var createdAt: LocalDateTime? = null

    var acceptedByVolunteer: String? = null
}
