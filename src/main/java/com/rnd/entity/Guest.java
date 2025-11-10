package com.rnd.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "guests")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String role;

    @Column(name = "invitation_status")
    private String invitationStatus = "invited";

    private String category;
    private String image;

    @Column(name = "event_id")
    private Long eventId;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getInvitationStatus() { return invitationStatus; }
    public void setInvitationStatus(String invitationStatus) { this.invitationStatus = invitationStatus; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
