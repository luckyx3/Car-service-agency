package com.hellcaster.Car.service.agency.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int startHour;

    @Column(nullable = false)
    private int endHour;

    @ManyToOne
    @JoinColumn(name = "service_operator_id", nullable = false)
    private ServiceOperator serviceOperator;
}
