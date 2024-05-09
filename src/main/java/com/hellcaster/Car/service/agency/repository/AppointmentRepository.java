package com.hellcaster.Car.service.agency.repository;

import com.hellcaster.Car.service.agency.entities.Appointment;
import com.hellcaster.Car.service.agency.entities.ServiceOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // List all appointments for the given service operator in sorted order based on start hour
    List<Appointment> findByServiceOperatorOrderByStartHourAsc(ServiceOperator serviceOperator);

    // List all Service Operators that have an appointment during the given time range
    @Query("SELECT DISTINCT a.serviceOperator FROM Appointment a WHERE a.startHour >= :startHour AND a.endHour <= :endHour")
    List<ServiceOperator> findServiceOperatorsByAppointmentTimeRange(@Param("startHour") int startHour, @Param("endHour") int endHour);
}
