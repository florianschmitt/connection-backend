package de.florianschmitt.model.entities

import javax.persistence.*

@Entity
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("VOLUNTEER_ID", "REQUEST_ID"))))
class EFeedback : BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    var volunteer: EVolunteer

    @ManyToOne
    @JoinColumn(nullable = false)
    var request: ERequest

    @Column(nullable = false)
    var positive: Boolean = false

    @Column(nullable = true, length = 1024)
    var comment: String? = null

    constructor(volunteer: EVolunteer, request: ERequest, positive: Boolean, comment: String?) {
        this.volunteer = volunteer
        this.request = request
        this.positive = positive
        this.comment = comment
    }
}
