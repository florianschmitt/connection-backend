package de.florianschmitt.model.entities

import org.hibernate.validator.constraints.NotBlank

import javax.persistence.*

@Entity
class EVoucher : BaseEntity {

    @Column(nullable = false, unique = true)
    @NotBlank
    var identifier: String

    @ManyToOne
    @JoinColumn(nullable = false)
    var volunteer: EVolunteer

    @ManyToOne
    @JoinColumn(nullable = false)
    var request: ERequest

    @Column(nullable = true)
    @Basic
    var answer: Boolean?

    constructor(identifier: String, volunteer: EVolunteer, request: ERequest) : this(identifier, volunteer, request, null)

    constructor(identifier: String, volunteer: EVolunteer, request: ERequest, answer: Boolean?) {
        this.identifier = identifier
        this.volunteer = volunteer
        this.request = request
        this.answer = answer
    }
}
