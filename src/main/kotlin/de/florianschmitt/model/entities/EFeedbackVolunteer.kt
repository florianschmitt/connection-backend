package de.florianschmitt.model.entities

import javax.persistence.*

@Entity
@Table
class EFeedbackVolunteer(request: ERequest,
                         hasOccurred: Boolean = false,
                         wasPositive: Boolean?,
                         wasCanceled: Boolean?,
                         comment: String?) : BaseEntity() {

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    var request: ERequest = request

    @Column(nullable = true)
    var wasPositive: Boolean? = wasPositive

    @Column(nullable = true)
    var wasCanceled: Boolean? = wasCanceled

    @Column(nullable = false)
    var hasOccurred: Boolean = hasOccurred

    @Column(nullable = true, length = 1024)
    var comment: String? = comment
}
