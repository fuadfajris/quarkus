package com.rnd.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public class CreateGuestScheduleRequest {

    @NotNull
    public Long guest_id;

    @NotNull
    public Long event_id;

    @NotNull
    public LocalDate schedule_date; // yyyy-MM-dd

    @NotNull
    public String start_time; // "HH:mm:ss"

    @NotNull
    public String end_time;

    public String stage;
}
