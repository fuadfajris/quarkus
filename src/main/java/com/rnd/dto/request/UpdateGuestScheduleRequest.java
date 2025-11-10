package com.rnd.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateGuestScheduleRequest {
    public Long guest_id;
    public Long event_id;
    public LocalDate schedule_date;
    public String start_time;
    public String end_time;
    public String stage;
}
