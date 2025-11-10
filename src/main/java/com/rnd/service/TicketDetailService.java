package com.rnd.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TicketDetailService {

    @Inject
    EntityManager em;

    public List<Map<String, Object>> getTicketDetailsByOrder(Long orderId) {
        String sql = "SELECT td.id AS td_id, td.name AS td_name, td.email AS td_email, td.phone AS td_phone, " +
                "td.address AS td_address, td.age AS td_age, td.gender AS td_gender, td.ticket_status AS td_ticket_status, " +
                "td.event_date AS td_event_date, td.order_id AS td_order_id, " +
                "o.id AS order_id, t.ticket_type AS ticket_type, t.price AS ticket_price, " +
                "c.id AS checkin_id, c.checked_in_at AS checked_in_at " +
                "FROM ticket_details td " +
                "JOIN orders o ON td.order_id = o.id " +
                "JOIN tickets t ON o.ticket_id = t.id " +
                "LEFT JOIN checkins c ON td.id = c.ticket_detail_id " +
                "WHERE td.order_id = :orderId";

        Query query = em.createNativeQuery(sql);
        query.setParameter("orderId", orderId);

        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> {
            // checkin object
            Map<String, Object> checkin = null;
            if (row[13] != null) {
                checkin = new HashMap<>();
                checkin.put("id", row[13]);
                checkin.put("checked_in_at", row[14]);
            }

            // order + ticket
            Map<String, Object> order = new HashMap<>();
            order.put("id", row[10]);
            Map<String, Object> ticket = new HashMap<>();
            ticket.put("ticket_type", row[11]);
            ticket.put("price", row[12]);
            order.put("ticket", ticket);

            // ticket detail
            Map<String, Object> td = new HashMap<>();
            td.put("id", row[0]);
            td.put("name", row[1]);
            td.put("email", row[2]);
            td.put("phone", row[3]);
            td.put("address", row[4]);
            td.put("age", row[5]);
            td.put("gender", row[6]);
            td.put("ticket_status", row[7]);
            td.put("event_date", row[8]);
            td.put("order_id", row[9]);
            td.put("order", order);
            td.put("checkin", checkin);

            return td;
        }).toList();
    }
}
