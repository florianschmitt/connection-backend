package de.florianschmitt.model.rest

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

import java.io.Serializable

class ESystemUserDTO : Serializable {

    var id: Long? = null

    @NotBlank
    var firstname: String? = null

    @NotBlank
    var lastname: String? = null

    var active: Boolean? = null

    var hasAdminRight: Boolean = false

    var hasFinanceRight: Boolean = false

    @Email
    @NotBlank
    var email: String? = null

    //    @Getter(onMethod = @__(@JsonIgnore))
    //    @Setter(onMethod = @__(@JsonIgnore(false)))
    //TODO:
    var cleartextPassword: String? = null
}
