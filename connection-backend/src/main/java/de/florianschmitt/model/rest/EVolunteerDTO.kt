package de.florianschmitt.model.rest

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.io.Serializable

class EVolunteerDTO : Serializable {

    var id: Long? = null

    @NotBlank
    var firstname: String? = null

    @NotBlank
    var lastname: String? = null

    var isActive: Boolean? = null

    @Email
    @NotBlank
    var email: String? = null

    @NotNull
    @Size(min = 1)
    var languageIds: Set<Long>? = null
}
