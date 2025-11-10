package com.rnd.service;

import com.rnd.entity.Activity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.*;

@ApplicationScoped
public class ActivityService {

    @Inject
    EntityManager em;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Check pending activity
     */
    public boolean checkPending(Long contentId, Long merchantId, String contentKey) {
        String sql = """
            SELECT content, status
            FROM activity
            WHERE merchant_id = :merchantId
              AND content_key = :contentKey
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("merchantId", merchantId);
        query.setParameter("contentKey", contentKey);

        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            String contentJson = (String) row[0];
            String status = (String) row[1];

            if ("pending".equalsIgnoreCase(status) && contentJson != null) {
                try {
                    Map<String, Object> contentMap = objectMapper.readValue(
                            contentJson, new TypeReference<Map<String, Object>>() {}
                    );
                    Map<String, Object> prevValue = (Map<String, Object>) contentMap.get("prevValue");
                    Map<String, Object> newValue = (Map<String, Object>) contentMap.get("newValue");

                    Long prevId = prevValue != null && prevValue.get("id") != null
                            ? Long.parseLong(prevValue.get("id").toString())
                            : null;
                    Long newId = newValue != null && newValue.get("id") != null
                            ? Long.parseLong(newValue.get("id").toString())
                            : null;

                    if (Objects.equals(prevId, contentId) || Objects.equals(newId, contentId)) {
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * Get all activities by merchant with pagination
     */
    public Map<String, Object> findAllByMerchant(Long merchantId, int page, int perPage) {
        // Hitung total
        Query countQuery = em.createNativeQuery(
                "SELECT COUNT(*) FROM activity WHERE merchant_id = :merchantId"
        );
        countQuery.setParameter("merchantId", merchantId);
        Number total = ((Number) countQuery.getSingleResult());

        int offset = (page - 1) * perPage;

        // Ambil data
        String sql = """
            SELECT *
            FROM activity
            WHERE merchant_id = :merchantId
            ORDER BY id DESC
            LIMIT :limit OFFSET :offset
        """;
        Query query = em.createNativeQuery(sql, Activity.class);
        query.setParameter("merchantId", merchantId);
        query.setParameter("limit", perPage);
        query.setParameter("offset", offset);

        List<Activity> data = query.getResultList();

        Map<String, Object> result = new HashMap<>();
        result.put("total", total.intValue());
        result.put("page", page);
        result.put("perPage", perPage);
        result.put("totalPages", (int) Math.ceil(total.doubleValue() / perPage));
        result.put("data", data);

        return result;
    }

    /**
     * Find one activity by id
     */
    public Activity findOne(Long id) {
        Activity activity = em.find(Activity.class, id);
        if (activity == null) {
            throw new NotFoundException("Activity not found");
        }
        return activity;
    }

    /**
     * Create new activity
     */
    @Transactional
    public Activity create(Map<String, Object> payload) {
        try {
            Map<String, Object> maker = (Map<String, Object>) payload.get("maker");
            Long merchantId = ((Number) maker.get("merchant_id")).longValue();
            String contentKey = (String) payload.get("content_key");
            String contentName = (String) payload.get("content_name");

            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("prevValue", payload.get("prevValue"));
            contentMap.put("newValue", payload.get("newValue"));
            String contentJson = objectMapper.writeValueAsString(contentMap);
            String makerJson = objectMapper.writeValueAsString(maker);

            Activity activity = new Activity();
            activity.setMerchantId(merchantId);
            activity.setContentKey(contentKey);
            activity.setContentName(contentName);
            activity.setContent(contentJson);
            activity.setMaker(makerJson);
            activity.setStatus("pending");
            activity.setCreatedAt(new Date());

            em.persist(activity);
            return activity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create activity", e);
        }
    }

    /**
     * Update activity (approve/reject)
     */
    @Transactional
    public Activity update(Long id, Map<String, Object> approver, String status) {
        Activity activity = findOne(id);

        Long approverMerchantId = ((Number) approver.get("merchant_id")).longValue();
        String role = ((String) approver.get("role")).toLowerCase();

        if (!activity.getMerchantId().equals(approverMerchantId)) {
            throw new RuntimeException("You are not authorized to update this activity (merchant mismatch)");
        }

        if (!"approver".equals(role)) {
            throw new RuntimeException("You are not authorized to approve/reject this activity");
        }

        try {
            activity.setStatus(status);
            activity.setApprover(objectMapper.writeValueAsString(approver));
            activity.setUpdatedAt(new Date());

            em.merge(activity);
            return activity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update activity", e);
        }
    }

    /**
     * Delete activity
     */
    @Transactional
    public Map<String, Object> delete(Long id) {
        Activity activity = findOne(id);
        em.remove(activity);
        return Map.of("message", "Activity deleted successfully");
    }
}
