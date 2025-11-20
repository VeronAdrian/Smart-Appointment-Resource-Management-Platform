package com.smartplatform.user.model;

import java.util.UUID;

public class Tenant {
    private String id;
    private String name;
    private String domain; // e.g., "acme-corp", "tech-startup"
    private String status; // ACTIVE, INACTIVE, SUSPENDED
    private String contactEmail;
    private String contactPhone;

    public Tenant(String name, String domain, String contactEmail) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.domain = domain;
        this.contactEmail = contactEmail;
        this.status = "ACTIVE";
        this.contactPhone = "";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}

