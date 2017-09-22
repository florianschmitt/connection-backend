package de.florianschmitt.model.rest;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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

    @java.beans.ConstructorProperties({"requestIdentifier", "languageIds", "datetime", "ocation", "street", "postalCode", "city", "email", "phone", "state", "createdAt", "acceptedByVolunteer"})
    public ERequestDTO(String requestIdentifier, Set<Long> languageIds, LocalDateTime datetime, String ocation, String street, String postalCode, String city, String email, String phone, String state, LocalDateTime createdAt, String acceptedByVolunteer) {
        this.requestIdentifier = requestIdentifier;
        this.languageIds = languageIds;
        this.datetime = datetime;
        this.ocation = ocation;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.createdAt = createdAt;
        this.acceptedByVolunteer = acceptedByVolunteer;
    }

    public ERequestDTO() {
    }

    public String getRequestIdentifier() {
        return this.requestIdentifier;
    }

    public Set<Long> getLanguageIds() {
        return this.languageIds;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public String getOcation() {
        return this.ocation;
    }

    public String getStreet() {
        return this.street;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getState() {
        return this.state;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public String getAcceptedByVolunteer() {
        return this.acceptedByVolunteer;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public void setLanguageIds(Set<Long> languageIds) {
        this.languageIds = languageIds;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public void setOcation(String ocation) {
        this.ocation = ocation;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAcceptedByVolunteer(String acceptedByVolunteer) {
        this.acceptedByVolunteer = acceptedByVolunteer;
    }

    public String toString() {
        return "ERequestDTO(requestIdentifier=" + this.getRequestIdentifier() + ", languageIds=" + this.getLanguageIds() + ", datetime=" + this.getDatetime() + ", ocation=" + this.getOcation() + ", street=" + this.getStreet() + ", postalCode=" + this.getPostalCode() + ", city=" + this.getCity() + ", email=" + this.getEmail() + ", phone=" + this.getPhone() + ", state=" + this.getState() + ", createdAt=" + this.getCreatedAt() + ", acceptedByVolunteer=" + this.getAcceptedByVolunteer() + ")";
    }
}
