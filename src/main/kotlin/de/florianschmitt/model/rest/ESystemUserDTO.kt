package de.florianschmitt.model.rest

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

import java.io.Serializable

class ESystemUserDTO(
        var id: Long?,

        @NotBlank
        var firstname: String?,

        @NotBlank
        var lastname: String?,

        var isActive: Boolean = false,
        var hasAdminRight: Boolean = false,
        var hasFinanceRight: Boolean = false,

        @Email
        @NotBlank
        var email: String?,
        var cleartextPassword: String? = null
) : Serializable