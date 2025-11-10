package com.rnd.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderService {

    @Inject
    EntityManager em;

    public List<Map<String, Object>> getOrdersByEvent(Long eventId) {
        String sql = """
            SELECT 
                o.id AS order_id, o.user_id, o.event_id, o.ticket_id, o.order_date, o.status, o.quantity, o.price,
                u.name AS user_name, u.email AS user_email, u.phone AS user_phone,
                e.start_date AS event_start_date,
                t.ticket_type AS ticket_type, t.price AS ticket_price, t.quantity_available, t.valid_from_date, t.valid_to_date, t.access_special_show,
                td.id AS td_id, td.event_date AS td_event_date, td.gender AS td_gender
            FROM orders o
            JOIN users u ON o.user_id = u.id
            JOIN events e ON o.event_id = e.id
            JOIN tickets t ON o.ticket_id = t.id
            LEFT JOIN ticket_details td ON td.order_id = o.id
            WHERE o.status = 'paid' AND o.event_id = :eventId
            ORDER BY o.order_date DESC
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("eventId", eventId);

        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> {
            // user object
            Map<String, Object> user = new HashMap<>();
            user.put("name", row[8]);
            user.put("email", row[9]);
            user.put("phone", row[10]);

            // event object
            Map<String, Object> event = new HashMap<>();
            event.put("start_date", row[11]);

            // ticket object
            Map<String, Object> ticket = new HashMap<>();
            ticket.put("ticket_type", row[12]);
            ticket.put("price", row[13]);
            ticket.put("quantity_available", row[14]);
            ticket.put("valid_from_date", row[15]);
            ticket.put("valid_to_date", row[16]);
            ticket.put("access_special_show", row[17]);

            // ticket detail object (nullable)
            Map<String, Object> ticketDetail = null;
            if (row[18] != null) {
                ticketDetail = new HashMap<>();
                ticketDetail.put("id", row[18]);
                ticketDetail.put("event_date", row[19]);
                ticketDetail.put("gender", row[20]);
            }

            // order object
            Map<String, Object> order = new HashMap<>();
            order.put("id", row[0]);
            order.put("user_id", row[1]);
            order.put("event_id", row[2]);
            order.put("ticket_id", row[3]);
            order.put("order_date", row[4]);
            order.put("status", row[5]);
            order.put("quantity", row[6]);
            order.put("price", row[7]);
            order.put("user", user);
            order.put("event", event);
            order.put("ticket", ticket);
            order.put("ticket_detail", ticketDetail);

            return order;
        }).toList();
    }

    public Map<String, Object> getOrdersByEvent(Long eventId, int page, int limit, String search) {
        int offset = (page - 1) * limit;

        // Count query
        String countSql = "SELECT COUNT(*) FROM orders o JOIN users u ON o.user_id = u.id " +
                "WHERE o.status = 'paid' AND o.event_id = :eventId ";
        if (search != null && !search.isEmpty()) {
            countSql += "AND LOWER(u.name) LIKE :search ";
        }
        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter("eventId", eventId);
        if (search != null && !search.isEmpty()) {
            countQuery.setParameter("search", "%" + search.toLowerCase() + "%");
        }
        Number totalCount = ((Number) countQuery.getSingleResult());

        // Data query
        String dataSql = "SELECT o.id, o.user_id, o.event_id, o.ticket_id, o.order_date, o.status, " +
                "o.quantity, o.price, u.name as user_name, u.email as user_email, u.phone as user_phone " +
                "FROM orders o JOIN users u ON o.user_id = u.id " +
                "WHERE o.status = 'paid' AND o.event_id = :eventId ";
        if (search != null && !search.isEmpty()) {
            dataSql += "AND LOWER(u.name) LIKE :search ";
        }
        dataSql += "ORDER BY o.order_date DESC LIMIT :limit OFFSET :offset";

        Query dataQuery = em.createNativeQuery(dataSql);
        dataQuery.setParameter("eventId", eventId);
        if (search != null && !search.isEmpty()) {
            dataQuery.setParameter("search", "%" + search.toLowerCase() + "%");
        }
        dataQuery.setParameter("limit", limit);
        dataQuery.setParameter("offset", offset);

        List<Object[]> results = dataQuery.getResultList();

        // Mapping result ke format JSON
        List<Map<String, Object>> orders = results.stream().map(row -> {
            Map<String, Object> user = new HashMap<>();
            user.put("name", row[8]);
            user.put("email", row[9]);
            user.put("phone", row[10]);

            Map<String, Object> order = new HashMap<>();
            order.put("id", row[0]);
            order.put("user_id", row[1]);
            order.put("event_id", row[2]);
            order.put("ticket_id", row[3]);
            order.put("order_date", row[4]);
            order.put("status", row[5]);
            order.put("quantity", row[6]);
            order.put("price", row[7]);
            order.put("user", user);
            return order;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", orders);

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", totalCount.intValue());
        pagination.put("page", page);
        pagination.put("limit", limit);
        pagination.put("totalPages", (int) Math.ceil((double) totalCount.intValue() / limit));

        response.put("pagination", pagination);
        return response;
    }
}
