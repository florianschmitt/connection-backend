package de.florianschmitt.model.rest

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

import java.io.Serializable

class ESystemUserDTO : Serializable {

    constructor()

    constructor(id: Long?, firstname: String?, lastname: String?, email: String?, cleartextPassword: String? = null, isActive: Boolean = false, hasAdminRight: Boolean = false, hasFinanceRight: Boolean = false) {
        this.id = id
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.isActive = isActive
        this.hasAdminRight = hasAdminRight
        this.hasFinanceRight = hasFinanceRight
        this.cleartextPassword = cleartextPassword;
    }

    var id: Long? = null

    @NotBlank
    var firstname: String? = null

    @NotBlank
    var lastname: String? = null

    var isActive: Boolean = false
    var hasAdminRight: Boolean = false
    var hasFinanceRight: Boolean = false

    @Email
    @NotBlank
    var email: String? = null
    var cleartextPassword: String? = null
}