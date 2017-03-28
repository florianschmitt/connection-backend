package de.florianschmitt.service.util;

import de.florianschmitt.model.entities.*;
import de.florianschmitt.model.rest.*;
import de.florianschmitt.repository.LanguageRepository;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DTOMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private LanguageRepository languageRepository;

    private EVoucherDTO map(EVoucher source) {
        EVoucherDTO result = mapper.map(source, EVoucherDTO.class);
        result.setVolunteerString(source.getVolunteer().toDisplayString());
        result.setRequestIdentifier(source.getRequest().getRequestIdentifier());
        return result;
    }

    private EPaymentDTO map(EPayment source) {
        EPaymentDTO result = mapper.map(source, EPaymentDTO.class);
        result.setPaymentBookedBy(source.getPaymentBookedBy().toDisplayString());
        result.setRequestId(source.getRequest().getRequestIdentifier());
        return result;
    }

    private ELanguageAdminDTO map(ELanguage source) {
        ELanguageAdminDTO result = mapper.map(source, ELanguageAdminDTO.class);

        List<ELocalizedDTO> list = source.getLocalized().stream()//
                .map(this::map)//
                .sorted(CustomELocalizedDTOComparator.INSTANCE)//
                .collect(Collectors.toList());

        result.setLocalized(list);

        return result;
    }

    private ELanguage map(ELanguageAdminDTO source) {
        ELanguage result = mapper.map(source, ELanguage.class);

        Set<ELocalized> col = source.getLocalized().stream()//
                .map(this::map)//
                .peek(loc -> loc.setLanguage(result))
                .collect(Collectors.toSet());
        result.setLocalized(col);

        return result;
    }

    private ELocalizedDTO map(ELocalized source) {
        ELocalizedDTO result = mapper.map(source, ELocalizedDTO.class);
        result.setLocale(source.getLocaleLanguage().name().toLowerCase());
        return result;
    }

    private ELocalized map(ELocalizedDTO source) {
        ELocalized result = mapper.map(source, ELocalized.class);
        result.setLocaleLanguage(ELocalizedLanguageEnum.Companion.create(source.getLocale()));
        return result;
    }

    private ERequest map(ERequestDTO source) {
        ERequest result = mapper.map(source, ERequest.class);
        result.setLanguages(source.getLanguageIds().stream()//
                .map(languageRepository::findOne)//
                .collect(Collectors.toSet()));
        return result;
    }

    private ERequestDTO map(ERequest source) {
        ERequestDTO result = mapper.map(source, ERequestDTO.class);
        result.setLanguageIds(source.getLanguages().stream()//
                .map(AbstractPersistable::getId)//
                .collect(Collectors.toSet()));
        if (source.getAcceptedByVolunteer() != null) {
            result.setAcceptedByVolunteer(source.getAcceptedByVolunteer().toDisplayString());
        }
        return result;
    }

    private EVolunteer map(EVolunteerDTO source) {
        EVolunteer result = mapper.map(source, EVolunteer.class);
        result.setLanguages(source.getLanguageIds().stream()//
                .map(languageRepository::findOne)//
                .collect(Collectors.toSet()));
        return result;
    }

    private EVolunteerDTO map(EVolunteer source) {
        EVolunteerDTO result = mapper.map(source, EVolunteerDTO.class);
        result.setLanguageIds(source.getLanguages().stream()//
                .map(AbstractPersistable::getId)//
                .collect(Collectors.toSet()));
        return result;
    }

    @SuppressWarnings("unchecked")
    public <D> D map(Object source, Class<D> destinationType) {
        if (EVolunteerDTO.class.equals(destinationType)) {
            return (D) map((EVolunteer) source);
        } else if (EVolunteer.class.equals(destinationType)) {
            return (D) map((EVolunteerDTO) source);
        } else if (ELanguageAdminDTO.class.equals(destinationType)) {
            return (D) map((ELanguage) source);
        } else if (EVoucherDTO.class.equals(destinationType)) {
            return (D) map((EVoucher) source);
        } else if (ELanguage.class.equals(destinationType) && ELanguageAdminDTO.class.isInstance(source)) {
            return (D) map((ELanguageAdminDTO) source);
        } else if (ERequestDTO.class.equals(destinationType)) {
            return (D) map((ERequest) source);
        } else if (ERequest.class.equals(destinationType)) {
            return (D) map((ERequestDTO) source);
        } else if (EPaymentDTO.class.equals(destinationType)) {
            return (D) map((EPayment) source);
        }
        D result = mapper.map(source, destinationType);
        return result;
    }

    public <D> Collection<D> map(@NonNull Collection<?> source, Class<D> destinationType) {
        return source.stream()//
                .map(x -> this.map(x, destinationType))//
                .collect(Collectors.toList());
    }

    public <D> List<D> map(@NonNull List<?> source, Class<D> destinationType) {
        return source.stream()//
                .map(x -> this.map(x, destinationType))//
                .collect(Collectors.toList());
    }

    public <D> Page<D> map(@NonNull Page<?> source, Class<D> destinationType) {
        return source.map(new Converter<Object, D>() {

            @Override
            public D convert(Object source) {
                return map(source, destinationType);
            }
        });
    }
}
