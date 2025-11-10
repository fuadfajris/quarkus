package com.rnd.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String email;

    @Column
    public String name;

    @Column
    public String phone;

    @OneToMany(mappedBy = "user")
    public List<Order> orders;
}
