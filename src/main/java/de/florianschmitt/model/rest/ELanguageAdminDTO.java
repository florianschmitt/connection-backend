package de.florianschmitt.model.rest;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ELanguageAdminDTO implements Serializable {

    private Long id;

    @NotBlank
    private String identifier;

    private int viewOrder;

    private List<ELocalizedDTO> localized;
}
