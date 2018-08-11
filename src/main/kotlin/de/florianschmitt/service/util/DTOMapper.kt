package de.florianschmitt.service.util

import de.florianschmitt.model.entities.*
import de.florianschmitt.model.rest.*
import de.florianschmitt.repository.LanguageRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class DTOMapper {

    @Autowired
    private lateinit var mapper: ModelMapper

    @Autowired
    private lateinit var languageRepository: LanguageRepository

    private fun map(source: EPayment): EPaymentDTO {
        val result = mapper.map(source, EPaymentDTO::class.java)
        result.paymentBookedBy = source.paymentBookedBy.displayString
        result.requestId = source.request.requestIdentifier
        return result
    }

    private fun map(source: ELanguage): ELanguageAdminDTO {
        val result = mapper.map(source, ELanguageAdminDTO::class.java)

        result.localized = source.localized
                ?.map { map(it) }
                ?.sortedWith(CustomELocalizedDTOComparator.INSTANCE)
                ?.toList()

        return result
    }

    private fun map(source: ELanguageAdminDTO): ELanguage {
        val result = mapper.map(source, ELanguage::class.java)

        result.localized = source.localized
                ?.map { map(it) }
                ?.onEach { it.language = result }
                ?.toSet()

        return result
    }

    private fun map(source: ELocalized): ELocalizedDTO {
        val result = mapper.map(source, ELocalizedDTO::class.java)
        result.locale = source.localeLanguage.name.toLowerCase()
        return result
    }

    private fun map(source: ELocalizedDTO): ELocalized {
        val result = mapper.map(source, ELocalized::class.java)
        result.localeLanguage = ELocalizedLanguageEnum.create(source.locale!!)
        return result
    }

    private fun map(source: ERequestDTO): ERequest {
        val result = mapper.map(source, ERequest::class.java)

        result.languages = source.languageIds
                ?.mapNotNull { languageRepository.findById(it).orElse(null) }
                ?.toMutableSet()

        return result
    }

    private fun map(source: ERequest): ERequestDTO {
        val result = mapper.map(source, ERequestDTO::class.java)
        result.languageIds = source.languages
                ?.map { it.id }
                ?.toSet()

        result.acceptedByVolunteer = source.acceptedByVolunteer?.displayString
        result.occasionString = source.occasion
        result.occasionEnum = null;

        return result
    }

    private fun map(source: EVolunteerDTO): EVolunteer {
        val result = mapper.map(source, EVolunteer::class.java)

        result.languages = source.languageIds
                ?.mapNotNull { languageRepository.findById(it).orElse(null) }
                ?.toMutableSet()

        return result
    }

    fun <D> map(source: Any, destinationType: Class<D>): D {
        return when (destinationType) {
            EVolunteerDTO::class.java -> (source as EVolunteer).toDto()
            EVolunteer::class.java -> map(source as EVolunteerDTO)
            ELanguageAdminDTO::class.java -> map(source as ELanguage)
            EVoucherDTO::class.java -> (source as EVoucher).toDto()
            ELanguage::class.java -> if (source is ELanguageAdminDTO) map(source) else mapper.map(source, destinationType)
            ERequestDTO::class.java -> map(source as ERequest)
            ERequest::class.java -> map(source as ERequestDTO)
            EPaymentDTO::class.java -> map(source as EPayment)
            else -> mapper.map(source, destinationType)
        } as D
    }

    fun <D> map(source: Collection<*>, destinationType: Class<D>) = source
            .filterNotNull()
            .map { this.map(it, destinationType) }
            .toList()

    fun <D> map(source: List<*>, destinationType: Class<D>) = map(source as Collection<*>, destinationType)

    fun <D> map(source: Page<*>, destinationType: Class<D>) = source.map { map(it as Any, destinationType) }
}

fun EVoucher.toDto() = EVoucherDTO(
        id = this.id,
        identifier = this.identifier,
        requestIdentifier = this.request.requestIdentifier,
        volunteerString = this.volunteer.displayString,
        answer = this.answer)

fun EVolunteer.toDto() = EVolunteerDTO(
        id = this.id,
        firstname = this.firstname,
        lastname = this.lastname,
        isActive = this.isActive,
        email = this.email,
        languageIds = this.languages?.map { it.id }?.toSet())

fun ESystemUser.toDto() = ESystemUserDTO(
        id = this.id,
        firstname = this.firstname,
        lastname = this.lastname,
        isActive = this.isActive,
        email = this.email,
        hasAdminRight = this.hasAdminRight,
        hasFinanceRight = this.hasFinanceRight)

fun ERequest.toSimpleDto(): ERequestSimpleDTO {
    var result = ERequestSimpleDTO()
    result.datetime = this.datetime;
    result.languageIds = this.languages?.map { it.id }?.toSet()
    result.city = this.city
    result.ocation = this.occasion
    result.postalCode = this.postalCode
    result.street = this.street
    return result
}