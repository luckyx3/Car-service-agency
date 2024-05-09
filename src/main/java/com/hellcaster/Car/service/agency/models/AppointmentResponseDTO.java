package com.hellcaster.Car.service.agency.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {

    private Long id;
    private int startHour;
    private int endHour;
    private String serviceOperatorName;
}
