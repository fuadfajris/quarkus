package com.rnd.service;

import com.rnd.entity.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TemplateService {

    @Inject
    EntityManager em;

    public List<TemplateResponse> findAll() {
        // Fetch semua template
        TypedQuery<Template> query = em.createQuery(
                "SELECT DISTINCT t FROM Template t LEFT JOIN FETCH t.features ORDER BY t.id ASC",
                Template.class
        );

        List<Template> templates = query.getResultList();

        // Map ke DTO untuk hanya include feature_name
        return templates.stream().map(t -> {
            TemplateResponse tr = new TemplateResponse();
            tr.setId(t.getId());
            tr.setTitle(t.getTitle());
            tr.setThumbnail(t.getThumbnail());
            tr.setCategory(t.getCategory());
            tr.setUrl(t.getUrl());
            tr.setDescription(t.getDescription());

            List<FeatureResponse> featureResponses = t.getFeatures()
                    .stream()
                    .map(f -> {
                        FeatureResponse fr = new FeatureResponse();
                        fr.setFeatureName(f.getFeatureName());
                        return fr;
                    }).collect(Collectors.toList());

            tr.setFeatures(featureResponses);

            return tr;
        }).collect(Collectors.toList());
    }

    // DTO inner class
    public static class TemplateResponse {
        private Long id;
        private String title;
        private String thumbnail;
        private String category;
        private String url;
        private String description;
        private List<FeatureResponse> features;

        // Getter & Setter
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getThumbnail() { return thumbnail; }
        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<FeatureResponse> getFeatures() { return features; }
        public void setFeatures(List<FeatureResponse> features) { this.features = features; }
    }

    public static class FeatureResponse {
        private String featureName;

        public String getFeatureName() { return featureName; }
        public void setFeatureName(String featureName) { this.featureName = featureName; }
    }
}
