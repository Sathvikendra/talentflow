package com.talentflow.api.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequest {
    @NotBlank(message = "Client code is required")
    private String clientCode;

    @NotBlank(message = "Client name is required")
    private String clientName;
    @NotBlank(message = "Industry is required")
    private String industry;
    @NotBlank(message = "Contact name is required")
    private String contactName;
    @Email(message = "Invalid email format")
    private String contactEmail;
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String contactPhone;
    private String notes;
    private Boolean isActive;

    // Getters and setters
    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}




