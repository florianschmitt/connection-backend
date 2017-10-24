package de.florianschmitt.model.entities

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import javax.persistence.*
import javax.validation.constraints.Size

@Inheritance
@DiscriminatorColumn(name = "TYPE")
@MappedSuperclass
abstract class EAbstractUser : BaseEntity() {

    @Column(nullable = false)
    @NotBlank
    var firstname: String = ""

    @Column(nullable = false)
    @NotBlank
    var lastname: String = ""

    @Column
    @Basic(optional = false)
    var isActive = true

    @Column(nullable = false, length = 250, unique = true)
    @Size(min = 5, max = 250)
    @Email
    @NotBlank
    var email: String = ""

    val displayString: String
        get() = "$lastname, $firstname"

    val germanDisplayString: String
        get() = "$firstname $lastname"
}
