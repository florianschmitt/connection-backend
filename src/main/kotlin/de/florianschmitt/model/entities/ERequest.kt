package de.florianschmitt.model.entities

import de.florianschmitt.model.entities.util.DateConverter
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class ERequest : BaseEntity() {

    @Column(nullable = false, unique = true)
    @NotBlank
    var requestIdentifier: String? = null

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    var state: ERequestStateEnum? = ERequestStateEnum.OPEN

    @ManyToMany(targetEntity = ELanguage::class)
    @Size(min = 1)
    var languages: MutableSet<ELanguage>? = null

    @Column(nullable = false)
    @NotNull
    var datetime: LocalDateTime? = null

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    var occasionEnum: EOccasionEnum? = EOccasionEnum.DOCTOR

    @Column(nullable = true)
    var occasionString: String? = null

    @Column(nullable = false)
    @NotBlank
    var street: String? = null

    @Column(nullable = false)
    @NotBlank
    var postalCode: String? = null

    @Column(nullable = false)
    @NotBlank
    var city: String? = null

    @Column(nullable = false)
    @NotBlank
    var requesterName: String? = null

    @Column(nullable = true)
    var requesterInstitution: String? = null

    @Email
    @Column(nullable = false)
    @NotBlank
    var email: String? = null

    @Column(nullable = false)
    @NotBlank
    var phone: String? = null

    @Column(nullable = false)
    @NotNull
    var createdAt: LocalDateTime? = null

    @JoinColumn
    @ManyToOne
    var acceptedByVolunteer: EVolunteer? = null

    @OneToMany(mappedBy = "request", targetEntity = EPayment::class)
    var payments: MutableSet<EPayment>? = null

    val dateString: String?
        get() = DateConverter.convertStandard(datetime)

    val languageString: String?
        get() = languages
                ?.map(ELanguage::valueInDefaultLanguage)
                ?.joinToString()

    val occasion: String?
        get() = when (occasionEnum) {
            EOccasionEnum.OTHER -> occasionString
            else -> occasionEnum.toString()
        }
}
