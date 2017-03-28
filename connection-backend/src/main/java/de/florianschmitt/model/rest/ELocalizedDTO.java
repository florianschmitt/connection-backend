package de.florianschmitt.model.rest;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ELocalizedDTO implements Serializable {

    private Long id;

    @NotBlank
    private String value;

    @NotBlank
    private String locale;
}
