package com.rnd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long merchant_id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String location;

    @Column
    private LocalDateTime start_date;

    @Column
    private LocalDateTime end_date;

    @Column
    private Integer capacity;

    @Column(nullable = false)
    private Boolean status = false;

    @Column(columnDefinition = "TEXT")
    private String image_venue;

    @Column(columnDefinition = "TEXT")
    private String hero_image;

    @Column
    private Integer template_id = 1;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMerchant_id() { return merchant_id; }
    public void setMerchant_id(Long merchant_id) { this.merchant_id = merchant_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getStart_date() { return start_date; }
    public void setStart_date(LocalDateTime start_date) { this.start_date = start_date; }

    public LocalDateTime getEnd_date() { return end_date; }
    public void setEnd_date(LocalDateTime end_date) { this.end_date = end_date; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getImage_venue() { return image_venue; }
    public void setImage_venue(String image_venue) { this.image_venue = image_venue; }

    public String getHero_image() { return hero_image; }
    public void setHero_image(String hero_image) { this.hero_image = hero_image; }

    public Integer getTemplate_id() { return template_id; }
    public void setTemplate_id(Integer template_id) { this.template_id = template_id; }
}
