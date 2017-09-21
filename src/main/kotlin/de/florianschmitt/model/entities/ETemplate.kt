package de.florianschmitt.model.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class ETemplate(identifier: String, description: String, template: String) : BaseEntity() {

    @Column(nullable = false, length = 100, unique = true)
    var identifier: String = identifier

    @Column(nullable = false, length = 1000)
    var description: String = description

    @Column(nullable = false, length = 10000)
    var template: String = template
}
