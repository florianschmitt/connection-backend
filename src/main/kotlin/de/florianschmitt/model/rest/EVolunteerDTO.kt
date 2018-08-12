package de.florianschmitt.model.rest

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class EVolunteerDTO : Serializable {
    constructor()

    constructor(id: Long?, firstname: String?, lastname: String?, email: String?, cleartextPassword: String? = null, isActive: Boolean = false, languageIds: Set<Long>? = null) {
        this.id = id
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.isActive = isActive
        this.languageIds = languageIds
    }

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