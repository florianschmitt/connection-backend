package de.florianschmitt.model.entities

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class EVolunteer : EAbstractUser() {

    @NotNull
    @Size(min = 1)
    @ManyToMany
    var languages: Set<ELanguage>? = null
}
