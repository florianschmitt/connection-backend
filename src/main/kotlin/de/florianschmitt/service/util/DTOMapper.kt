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

    private fun map(source: EVoucher): EVoucherDTO {
        val result = mapper.map(source, EVoucherDTO::class.java)
        result.volunteerString = source.volunteer.toDisplayString()
        result.requestIdentifier = source.request.requestIdentifier
        return result
    }

    private fun map(source: EPayment): EPaymentDTO {
        val result = mapper.map(source, EPaymentDTO::class.java)
        result.paymentBookedBy = source.paymentBookedBy.toDisplayString()
        result.requestId = source.request.requestIdentifier
        return result
    }

    private fun map(source: ELanguage): ELanguageAdminDTO {
        val result = mapper.map(source, ELanguageAdminDTO::class.java)

        result.localized = source.localized
                ?.map { this.map(it) }
                ?.sortedWith(CustomELocalizedDTOComparator.INSTANCE)
                ?.toList()

        return result
    }

    private fun map(source: ELanguageAdminDTO): ELanguage {
        val result = mapper.map(source, ELanguage::class.java)

        result.localized = source.localized
                ?.map { this.map(it) }
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

        result.acceptedByVolunteer = source.acceptedByVolunteer?.toDisplayString()

        return result
    }

    private fun map(source: EVolunteerDTO): EVolunteer {
        val result = mapper.map(source, EVolunteer::class.java)

        result.languages = source.languageIds
                ?.mapNotNull { languageRepository.findById(it).orElse(null) }
                ?.toMutableSet()

        return result
    }

    private fun map(source: EVolunteer): EVolunteerDTO {
        val result = mapper.map(source, EVolunteerDTO::class.java)

        result.languageIds = source.languages
                ?.map { it.id }
                ?.toSet()

        return result
    }

    fun <D> map(source: Any, destinationType: Class<D>): D {
        if (EVolunteerDTO::class.java == destinationType) {
            return map(source as EVolunteer) as D
        } else if (EVolunteer::class.java == destinationType) {
            return map(source as EVolunteerDTO) as D
        } else if (ELanguageAdminDTO::class.java == destinationType) {
            return map(source as ELanguage) as D
        } else if (EVoucherDTO::class.java == destinationType) {
            return map(source as EVoucher) as D
        } else if (ELanguage::class.java == destinationType && ELanguageAdminDTO::class.java.isInstance(source)) {
            return map(source as ELanguageAdminDTO) as D
        } else if (ERequestDTO::class.java == destinationType) {
            return map(source as ERequest) as D
        } else if (ERequest::class.java == destinationType) {
            return map(source as ERequestDTO) as D
        } else if (EPaymentDTO::class.java == destinationType) {
            return map(source as EPayment) as D
        }
        return mapper.map(source, destinationType)
    }

    fun <D> map(source: Collection<*>, destinationType: Class<D>) = source
            .filterNotNull()
            .map { this.map(it, destinationType) }
            .toList()

    fun <D> map(source: List<*>, destinationType: Class<D>) = source
            .filterNotNull()
            .map { this.map(it, destinationType) }
            .toList()

    fun <D> map(source: Page<*>, destinationType: Class<D>) = source.map { x -> map(x as Any, destinationType) }
}
