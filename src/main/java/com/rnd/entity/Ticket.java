package com.rnd.entity;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String ticketType;

    @Column(nullable = false)
    public BigDecimal price;

    @Column(nullable = false)
    public Integer quantityAvailable;

    @Column
    public java.util.Date validFromDate;

    @Column
    public java.util.Date validToDate;

    @Column
    public Boolean accessSpecialShow = false;

    @ManyToOne
    @JoinColumn(name = "event_id")
    public Event event;

    @OneToMany(mappedBy = "ticket")
    public List<Order> orders;
}
