package com.rnd.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.*;

@ApplicationScoped
public class CheckinService {

    @Inject
    EntityManager em;

    /**
     * Get checkins by event (like getCheckins)
     */
    public List<Map<String, Object>> getCheckinsByEvent(Long eventId) {
        String sql = """
            SELECT c.id AS checkin_id, c.checked_in_at,
                   td.id AS td_id, td.gender, td.order_id,
                   o.event_id
            FROM checkins c
            JOIN ticket_details td ON td.id = c.ticket_detail_id
            JOIN orders o ON td.order_id = o.id
            WHERE c.checked_in_at IS NOT NULL
              AND o.event_id = :eventId
            ORDER BY c.id ASC
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("eventId", eventId);

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> order = new HashMap<>();
            order.put("id", row[5]);
            order.put("event_id", row[6]);

            Map<String, Object> ticketDetail = new HashMap<>();
            ticketDetail.put("id", row[2]);
            ticketDetail.put("gender", row[3]);
            ticketDetail.put("order", order);

            Map<String, Object> checkin = new HashMap<>();
            checkin.put("id", row[0]);
            checkin.put("checked_in_at", row[1]);
            checkin.put("ticket_detail", ticketDetail);

            response.add(checkin);
        }

        return response;
    }

    /**
     * Create new checkin
     */
    @Transactional
    public Map<String, Object> createCheckin(Long ticketDetailId) {
        // Check if already exists
        String checkSql = "SELECT COUNT(*) FROM checkins WHERE ticket_detail_id = :tdId";
        Query checkQuery = em.createNativeQuery(checkSql);
        checkQuery.setParameter("tdId", ticketDetailId);
        Number count = (Number) checkQuery.getSingleResult();
        if (count.intValue() > 0) {
            throw new BadRequestException("Ticket sudah pernah di-checkin");
        }

        String insertSql = """
            INSERT INTO checkins (ticket_detail_id, checked_in_at)
            VALUES (:tdId, CURRENT_TIMESTAMP)
        """;
        Query insertQuery = em.createNativeQuery(insertSql);
        insertQuery.setParameter("tdId", ticketDetailId);
        insertQuery.executeUpdate();

        Map<String, Object> response = new HashMap<>();
        response.put("ticket_detail_id", ticketDetailId);
        response.put("checked_in_at", new Date());
        return response;
    }

    /**
     * Get all checkins (like getCheckinsV2)
     */
    public List<Map<String, Object>> getCheckinsByEventV2(Long eventId) {
        String sql = """
            SELECT c.id AS checkin_id, c.checked_in_at,
                   td.id AS td_id, td.name, td.email, td.phone, td.age, td.gender, td.ticket_status, td.event_date,
                   o.id AS order_id, o.event_id
            FROM checkins c
            JOIN ticket_details td ON td.id = c.ticket_detail_id
            JOIN orders o ON td.order_id = o.id
            WHERE o.event_id = :eventId
            ORDER BY c.id ASC
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("eventId", eventId);

        List<Object[]> results = query.getResultList();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> order = new HashMap<>();
            order.put("id", row[10]);
            order.put("event_id", row[11]);

            Map<String, Object> ticketDetail = new HashMap<>();
            ticketDetail.put("id", row[2]);
            ticketDetail.put("name", row[3]);
            ticketDetail.put("email", row[4]);
            ticketDetail.put("phone", row[5]);
            ticketDetail.put("age", row[6]);
            ticketDetail.put("gender", row[7]);
            ticketDetail.put("ticket_status", row[8]);
            ticketDetail.put("event_date", row[9]);
            ticketDetail.put("order", order);

            Map<String, Object> checkin = new HashMap<>();
            checkin.put("id", row[0]);
            checkin.put("checked_in_at", row[1]);
            checkin.put("ticket_detail", ticketDetail);

            response.add(checkin);
        }

        return response;
    }
}
