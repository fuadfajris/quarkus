package com.rnd.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    public Event event;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    public Ticket ticket;

    @Column(nullable = false)
    public Date orderDate;

    @Column(nullable = false)
    public String status;

    @Column(nullable = false)
    public Integer quantity;

    @Column(nullable = false)
    public BigDecimal price;

    @OneToMany(mappedBy = "order")
    public List<TicketDetail> ticketDetails;
}
