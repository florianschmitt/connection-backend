package de.florianschmitt.model.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class ETemplate : BaseEntity {

    @Column(nullable = false, length = 100, unique = true)
    var identifier: String

    @Column(nullable = false, length = 1000)
    var description: String

    @Column(nullable = false, length = 10000)
    var template: String

    constructor(identifier: String, description: String, template: String) {
        this.identifier = identifier
        this.description = description
        this.template = template
    }
}
