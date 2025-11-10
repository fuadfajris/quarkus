package com.rnd.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ticket_details")
public class TicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String phone;

    @Column
    public String address;

    @Column
    public Integer age;

    @Column
    public String gender;

    @Column
    public String ticketStatus = "valid";

    @Column
    public Date eventDate;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;

    @OneToOne(mappedBy = "ticketDetail")
    public Checkin checkin;
}
