package de.florianschmitt.model.rest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERequestDTO implements Serializable {

    private String requestIdentifier;

    @NotNull
    @Size(min = 1)
    private Set<Long> languageIds;

    @NotNull
    private LocalDateTime datetime;

    @NotBlank
    private String ocation;

    @NotBlank
    private String street;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String city;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    private String state;

    private LocalDateTime createdAt;

    private String acceptedByVolunteer;
}
