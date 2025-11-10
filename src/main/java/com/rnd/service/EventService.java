package com.rnd.service;

import com.rnd.entity.Event;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class EventService {

    @Inject
    EntityManager em;

    public Event findEvent(Long eventId, Long merchantId) {
        TypedQuery<Event> query = em.createQuery(
                "SELECT e FROM Event e WHERE e.id = :id AND e.merchant_id = :merchantId", Event.class
        );
        query.setParameter("id", eventId);
        query.setParameter("merchantId", merchantId);

        List<Event> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public Map<String, Object> findEventsByMerchant(Long merchantId, int page, int perPage, String search) {
        StringBuilder sb = new StringBuilder("SELECT e FROM Event e WHERE e.merchant_id = :merchantId");
        if (search != null && !search.isEmpty()) {
            sb.append(" AND LOWER(e.name) LIKE :search");
        }
        sb.append(" ORDER BY e.id DESC");

        TypedQuery<Event> query = em.createQuery(sb.toString(), Event.class);
        query.setParameter("merchantId", merchantId);
        if (search != null && !search.isEmpty()) {
            query.setParameter("search", "%" + search.toLowerCase() + "%");
        }

        // Count total
        StringBuilder countSb = new StringBuilder("SELECT COUNT(e) FROM Event e WHERE e.merchant_id = :merchantId");
        if (search != null && !search.isEmpty()) {
            countSb.append(" AND LOWER(e.name) LIKE :search");
        }

        TypedQuery<Long> countQuery = em.createQuery(countSb.toString(), Long.class);
        countQuery.setParameter("merchantId", merchantId);
        if (search != null && !search.isEmpty()) {
            countQuery.setParameter("search", "%" + search.toLowerCase() + "%");
        }

        Long total = countQuery.getSingleResult();

        if (perPage != 0) {
            int offset = (page - 1) * perPage;
            query.setFirstResult(offset);
            query.setMaxResults(perPage);
        }

        List<Event> events = query.getResultList();
        int perPageResponse = (perPage == 0) ? events.size() : perPage;
        int totalPages = (perPage == 0) ? 1 : (int) Math.ceil(total / (double) perPage);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", events);
        resultMap.put("total", total);
        resultMap.put("page", page);
        resultMap.put("perPage", perPageResponse);
        resultMap.put("totalPages", totalPages);

        return resultMap;
    }

    @Transactional
    public Event createEvent(Event event) {
        em.persist(event);
        return event;
    }

    @Transactional
    public Event updateEvent(Long eventId, Event eventData) throws Exception {
        Event event = em.find(Event.class, eventId);
        if (event == null) {
            throw new Exception("Event not found");
        }

        if (eventData.getName() != null) event.setName(eventData.getName());
        if (eventData.getDescription() != null) event.setDescription(eventData.getDescription());
        if (eventData.getLocation() != null) event.setLocation(eventData.getLocation());
        if (eventData.getStart_date() != null) event.setStart_date(eventData.getStart_date());
        if (eventData.getEnd_date() != null) event.setEnd_date(eventData.getEnd_date());
        if (eventData.getCapacity() != null) event.setCapacity(eventData.getCapacity());
        if (eventData.getStatus() != null) event.setStatus(eventData.getStatus());
        if (eventData.getImage_venue() != null) event.setImage_venue(eventData.getImage_venue());
        if (eventData.getHero_image() != null) event.setHero_image(eventData.getHero_image());
        if (eventData.getTemplate_id() != null) event.setTemplate_id(eventData.getTemplate_id());

        return event;
    }
}
