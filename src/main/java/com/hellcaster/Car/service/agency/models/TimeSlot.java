package com.hellcaster.Car.service.agency.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TimeSlot {
    private int startHour;
    private int endHour;
}
