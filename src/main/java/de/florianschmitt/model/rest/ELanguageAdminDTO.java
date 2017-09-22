package de.florianschmitt.model.rest;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class ELanguageAdminDTO implements Serializable {

    private Long id;

    @NotBlank
    private String identifier;

    private int viewOrder;

    private List<ELocalizedDTO> localized;

    @java.beans.ConstructorProperties({"id", "identifier", "viewOrder", "localized"})
    public ELanguageAdminDTO(Long id, String identifier, int viewOrder, List<ELocalizedDTO> localized) {
        this.id = id;
        this.identifier = identifier;
        this.viewOrder = viewOrder;
        this.localized = localized;
    }

    public ELanguageAdminDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public int getViewOrder() {
        return this.viewOrder;
    }

    public List<ELocalizedDTO> getLocalized() {
        return this.localized;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setViewOrder(int viewOrder) {
        this.viewOrder = viewOrder;
    }

    public void setLocalized(List<ELocalizedDTO> localized) {
        this.localized = localized;
    }
}
