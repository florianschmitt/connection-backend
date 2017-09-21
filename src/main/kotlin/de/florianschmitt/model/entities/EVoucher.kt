package de.florianschmitt.model.entities

import org.hibernate.validator.constraints.NotBlank

import javax.persistence.*

@Entity
class EVoucher(identifier: String, volunteer: EVolunteer, request: ERequest) : BaseEntity() {

    @Column(nullable = false, unique = true)
    @NotBlank
    var identifier: String = identifier

    @ManyToOne
    @JoinColumn(nullable = false)
    var volunteer: EVolunteer = volunteer

    @ManyToOne
    @JoinColumn(nullable = false)
    var request: ERequest = request

    @Column(nullable = true)
    @Basic
    var answer: Boolean? = null
}
