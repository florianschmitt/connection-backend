package de.florianschmitt.model.rest

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class EVolunteerDTO(
        var id: Long?,

        @NotBlank
        var firstname: String?,

        @NotBlank
        var lastname: String?,

        var isActive: Boolean?,

        @Email
        @NotBlank
        var email: String?,

        @NotNull
        @Size(min = 1)
        var languageIds: Set<Long>?
) : Serializable