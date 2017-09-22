package de.florianschmitt.model.rest;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class ELocalizedDTO implements Serializable {

    private Long id;

    @NotBlank
    private String value;

    @NotBlank
    private String locale;

    @java.beans.ConstructorProperties({"id", "value", "locale"})
    public ELocalizedDTO(Long id, String value, String locale) {
        this.id = id;
        this.value = value;
        this.locale = locale;
    }

    public ELocalizedDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
