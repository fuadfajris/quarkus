package com.rnd.service;

import com.rnd.entity.MerchantUser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MerchantUserService {

    @Inject
    EntityManager em;

    public Map<String, Object> login(String email, String password) {
        TypedQuery<MerchantUser> query = em.createQuery(
                "SELECT m FROM MerchantUser m WHERE m.email = :email", MerchantUser.class
        );
        query.setParameter("email", email);

        MerchantUser user;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            return null;
        }

        // cek password
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (!result.verified) {
            return null;
        }

        String token = Jwt.issuer("lifestyle-api")
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .expiresIn(Duration.ofHours(24))
                .sign();

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getEmail());
        userResponse.put("merchant_id", user.getMerchant_id());
        userResponse.put("logo", user.getLogo());
        userResponse.put("role", user.getRole().getRole_name());

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("access_token", token);
        response.put("user", userResponse);

        return response;
    }

    public MerchantUser findMerchant(Long merchantUsersId) throws Exception {
        MerchantUser merchant = em.find(MerchantUser.class, merchantUsersId);
        if (merchant == null) {
            throw new Exception("Merchant user with id " + merchantUsersId + " not found");
        }
        return merchant;
    }

    @Transactional
    public MerchantUser updateMerchantProfile(Long merchantId, String name, String email, String password, String logo) throws Exception {
        MerchantUser merchant = em.find(MerchantUser.class, merchantId);
        if (merchant == null) {
            throw new Exception("Merchant with id " + merchantId + " not found");
        }

        if (name != null) merchant.setName(name);
        if (email != null) merchant.setEmail(email);
        if (password != null && !password.isEmpty()) {
            String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            merchant.setPassword(hashed);
        }
        if (logo != null) merchant.setLogo(logo);
        merchant.setUpdated_at(LocalDateTime.now());

        return merchant;
    }
}
