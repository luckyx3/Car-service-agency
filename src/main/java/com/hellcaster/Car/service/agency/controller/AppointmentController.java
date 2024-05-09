package com.hellcaster.Car.service.agency.controller;

import com.hellcaster.Car.service.agency.entities.Appointment;
import com.hellcaster.Car.service.agency.models.AppointmentResponseDTO;
import com.hellcaster.Car.service.agency.models.TimeSlot;
import com.hellcaster.Car.service.agency.service.AppointmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@Log4j2
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(@RequestParam(required = false) String serviceOperatorName,
                                                                  @RequestParam int startHour,
                                                                  @RequestParam int endHour) {
        log.info("Book appointment request received: startHour=" + startHour + ", endHour=" + endHour);
        if(serviceOperatorName != null){
            AppointmentResponseDTO appointment = appointmentService.bookAppointmentForSpecificOperator(serviceOperatorName, startHour, endHour);
            return ResponseEntity.ok(appointment);
        }else{
            AppointmentResponseDTO appointment = appointmentService.bookAppointment(startHour, endHour);
            return ResponseEntity.ok(appointment);
        }
    }

    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(@PathVariable Long appointmentId,
                                                      @RequestParam int newStartHour,
                                                      @RequestParam int newEndHour) {
        log.info("Reschedule appointment request received: appointmentId=" + appointmentId + ", newStartHour=" + newStartHour + ", newEndHour=" + newEndHour);
        AppointmentResponseDTO rescheduledAppointment = appointmentService.rescheduleAppointment(appointmentId, newStartHour, newEndHour);
        return ResponseEntity.ok(rescheduledAppointment);
    }

    @DeleteMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        log.info("Cancel appointment request received: appointmentId=" + appointmentId);
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/operator/{serviceOperatorName}/booked")
    public ResponseEntity<List<AppointmentResponseDTO>> getBookedAppointmentsForOperator(@PathVariable String serviceOperatorName) {
        log.info("Get booked appointments request received: serviceOperatorName=" + serviceOperatorName);
        List<AppointmentResponseDTO> appointments = appointmentService.getBookedAppointmentsForOperator(serviceOperatorName);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/operator/{serviceOperatorName}/available")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlotsForOperator(@PathVariable String serviceOperatorName) {
        log.info("Get available time slots request received: serviceOperatorName=" + serviceOperatorName);
        List<TimeSlot> timeSlots = appointmentService.getAvailableTimeSlotsForOperator(serviceOperatorName);
        return ResponseEntity.ok(timeSlots);
    }
}
