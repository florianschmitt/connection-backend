package de.florianschmitt.model.rest

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.EVolunteer
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import java.io.Serializable
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class EVoucherDTO : Serializable {

    var id: Long? = null
    var identifier: String? = null
    var volunteerString: String? = null
    var requestIdentifier: String? = null
    var answer: Boolean? = null
}