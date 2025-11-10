package com.rnd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_users")
public class MerchantUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long merchant_id;

    @Column(nullable = false)
    private Long role_id;

    @Column
    private String logo;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getMerchant_id() { return merchant_id; }
    public void setMerchant_id(Long merchant_id) { this.merchant_id = merchant_id; }

    public Long getRole_id() { return role_id; }
    public void setRole_id(Long role_id) { this.role_id = role_id; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }
}
