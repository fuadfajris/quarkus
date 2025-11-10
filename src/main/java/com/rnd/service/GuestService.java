package com.rnd.service;

import com.rnd.dto.request.CreateGuestScheduleRequest;
import com.rnd.dto.request.UpdateGuestScheduleRequest;
import com.rnd.entity.Guest;
import com.rnd.entity.GuestSchedule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class GuestService {

    @Inject
    EntityManager em;

    public List<Guest> fetchAllLineup() {
        TypedQuery<Guest> query = em.createQuery("SELECT g FROM Guest g ORDER BY g.id ASC", Guest.class);
        return query.getResultList();
    }

    public List<GuestSchedule> fetchLineupByEventId(Long eventId) {
        String jpql = "SELECT gs FROM GuestSchedule gs " +
                "JOIN FETCH gs.guest g " +
                "WHERE gs.eventId = :eventId " +
                "ORDER BY gs.startTime ASC";
        TypedQuery<GuestSchedule> query = em.createQuery(jpql, GuestSchedule.class);
        query.setParameter("eventId", eventId);
        return query.getResultList();
    }

    public GuestSchedule findDetailById(Long guestScheduleId) {
        String jpql = "SELECT gs FROM GuestSchedule gs JOIN FETCH gs.guest g WHERE gs.id = :id";
        TypedQuery<GuestSchedule> query = em.createQuery(jpql, GuestSchedule.class);
        query.setParameter("id", guestScheduleId);

        List<GuestSchedule> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Transactional
    public GuestSchedule createSchedule(CreateGuestScheduleRequest dto) {
        GuestSchedule schedule = new GuestSchedule();
        schedule.setGuestId(dto.guest_id);
        schedule.setEventId(dto.event_id);
        schedule.setScheduleDate(dto.schedule_date);
        schedule.setStartTime(LocalTime.parse(dto.start_time));
        schedule.setEndTime(LocalTime.parse(dto.end_time));
        schedule.setStage(dto.stage);

        em.persist(schedule);
        return schedule;
    }

    @Transactional
    public GuestSchedule updateSchedule(Long id, UpdateGuestScheduleRequest dto) {

        GuestSchedule schedule = em.createQuery(
                        "SELECT gs FROM GuestSchedule gs JOIN FETCH gs.guest WHERE gs.id = :id",
                        GuestSchedule.class
                )
                .setParameter("id", id)
                .getSingleResult();

        if (schedule == null) {
            throw new NotFoundException("Guest schedule with ID " + id + " not found");
        }

        if (dto.guest_id != null) schedule.setGuestId(dto.guest_id);
        if (dto.event_id != null) schedule.setEventId(dto.event_id);
        if (dto.schedule_date != null) schedule.setScheduleDate(dto.schedule_date);
        if (dto.start_time != null) schedule.setStartTime(LocalTime.parse(dto.start_time));
        if (dto.end_time != null) schedule.setEndTime(LocalTime.parse(dto.end_time));
        if (dto.stage != null) schedule.setStage(dto.stage);

        em.merge(schedule);
        return schedule;
    }

    @Transactional
    public void deleteSchedule(Long id) {
        GuestSchedule schedule = em.find(GuestSchedule.class, id);

        if (schedule == null) {
            throw new NotFoundException("Guest schedule with ID " + id + " not found");
        }

        em.remove(schedule);
    }
}
