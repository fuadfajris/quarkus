package com.rnd.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "checkins")
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    @JoinColumn(name = "ticket_detail_id", nullable = false)
    public TicketDetail ticketDetail;

    @Column(name = "checked_in_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date checkedInAt = new Date();
}
