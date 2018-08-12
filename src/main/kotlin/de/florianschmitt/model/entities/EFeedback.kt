package de.florianschmitt.model.entities

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["VOLUNTEER_ID", "REQUEST_ID"]))])
class EFeedback(volunteer: EVolunteer, request: ERequest, positive: Boolean = false, comment: String?)  : BaseEntity() {

    @ManyToOne
    @JoinColumn(nullable = false)
    var volunteer: EVolunteer = volunteer

    @OneToOne
    @JoinColumn(nullable = false)
    var request: ERequest = request

    @Column(nullable = false)
    var positive: Boolean = positive

    @Column(nullable = true, length = 1024)
    var comment: String? = comment
}
