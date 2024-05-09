package com.hellcaster.Car.service.agency.service;

import com.hellcaster.Car.service.agency.entities.Appointment;
import com.hellcaster.Car.service.agency.entities.ServiceOperator;
import com.hellcaster.Car.service.agency.exceptions.CustomException;
import com.hellcaster.Car.service.agency.models.AppointmentResponseDTO;
import com.hellcaster.Car.service.agency.models.TimeSlot;
import com.hellcaster.Car.service.agency.repository.AppointmentRepository;
import com.hellcaster.Car.service.agency.repository.ServiceOperatorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Log4j2
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ServiceOperatorRepository serviceOperatorRepository;


    public AppointmentResponseDTO bookAppointmentForSpecificOperator(String serviceOperatorName, int startHour, int endHour) {
        log.info("Book appointment for specific operator service is called: serviceOperatorName=" + serviceOperatorName + ", startHour=" + startHour + ", endHour=" + endHour);

        // Check if the service operator is busy during the requested time range
        List<ServiceOperator> busyServiceOperators = appointmentRepository.findServiceOperatorsByAppointmentTimeRange(startHour, endHour);
        busyServiceOperators.stream().map(ServiceOperator::getName)
                .forEach((busyOperator) -> {
                            if(busyOperator.equals(serviceOperatorName)) {
                                throw new CustomException("Service operator is busy at this time. Please choose another time.", "SERVICE_OPERATOR_BUSY");
                            }
                        }
        );

        // Find the service operator by name
        ServiceOperator serviceOperator1 = serviceOperatorRepository.findByName(serviceOperatorName)
                .orElseThrow(() -> new CustomException("Invalid service operator name", "INVALID_SERVICE_OPERATOR"));

        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setStartHour(startHour);
        appointment.setEndHour(endHour);
        appointment.setServiceOperator(serviceOperator1);

        appointmentRepository.save(appointment);

        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .startHour(appointment.getStartHour())
                .endHour(appointment.getEndHour())
                .serviceOperatorName(appointment.getServiceOperator().getName())
                .build();
    }

    public AppointmentResponseDTO bookAppointment(int startHour, int endHour) {
        log.info("Book appointment service is called : startHour=" + startHour + ", endHour=" + endHour);

        // Get the list of busy service operators during the requested time range
        List<ServiceOperator> busyServiceOperators = appointmentRepository.findServiceOperatorsByAppointmentTimeRange(startHour, endHour);
        if(!busyServiceOperators.isEmpty()){

            // Find all service operator in the system and mark them as available
            HashMap<String, Boolean> allOperators = serviceOperatorRepository.findAll()
                    .stream().map(ServiceOperator::getName)
                    .collect(HashMap::new, (m, s) -> m.put(s, true), HashMap::putAll);

            // Find available service operators at the requested time
            for(var operator : busyServiceOperators) {
                allOperators.put(operator.getName(), false);
            }

            String availabelOperatorname = "";

            // Find the first available service operator
            for (var entry : allOperators.entrySet()) {
                if(entry.getValue()){
                    availabelOperatorname = entry.getKey();
                    break;
                }
            }
            if(!availabelOperatorname.isEmpty()){
                // If an available service operator is found, book the appointment
                ServiceOperator serviceOperator1 = serviceOperatorRepository.findByName(availabelOperatorname)
                        .orElseThrow(() -> new CustomException("Invalid service operator name", "INVALID_SERVICE_OPERATOR"));

                Appointment appointment = new Appointment();
                appointment.setStartHour(startHour);
                appointment.setEndHour(endHour);
                appointment.setServiceOperator(serviceOperator1);

                appointmentRepository.save(appointment);

                return AppointmentResponseDTO.builder()
                        .id(appointment.getId())
                        .startHour(appointment.getStartHour())
                        .endHour(appointment.getEndHour())
                        .serviceOperatorName(appointment.getServiceOperator().getName())
                        .build();
            }
            else{
                // If no available service operator is found, throw an exception
                throw new CustomException("All service operators are busy at this time. Please choose another time.", "ALL_OPERATORS_BUSY");
            }
        }
        else{
            // If no service operators are busy, book the appointment with the default service operator
            ServiceOperator defaultServiceOperator = getDefaultServiceOperator();
            Appointment appointment = new Appointment();
            appointment.setStartHour(startHour);
            appointment.setEndHour(endHour);
            appointment.setServiceOperator(defaultServiceOperator);

            appointmentRepository.save(appointment);

            return AppointmentResponseDTO.builder()
                    .id(appointment.getId())
                    .startHour(appointment.getStartHour())
                    .endHour(appointment.getEndHour())
                    .serviceOperatorName(appointment.getServiceOperator().getName())
                    .build();
        }
    }

    public AppointmentResponseDTO rescheduleAppointment(Long appointmentId, int newStartHour, int newEndHour) {
        log.info("Reschedule appointment service is called: appointmentId=" + appointmentId + ", newStartHour=" + newStartHour + ", newEndHour=" + newEndHour);

        // Find the appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new CustomException("No Appointment found with appointment ID", "INVALID_APPOINTMENT_ID"));

        // Check if the new start hour is less than or equal to the current hour
        if(appointment.getStartHour() >= newStartHour){
            throw new CustomException("Start hour cannot be less than or equal to current hour", "INVALID_TIME_SLOT");
        }

        // Get the list of busy service operators during the requested time range
        List<ServiceOperator> busyServiceOperators = appointmentRepository.findServiceOperatorsByAppointmentTimeRange(newStartHour, newEndHour);
        if(!busyServiceOperators.isEmpty()){

            // Find all service operator in the system and mark them as available
            HashMap<String, Boolean> allOperators = serviceOperatorRepository.findAll()
                    .stream().map(ServiceOperator::getName)
                    .collect(HashMap::new, (m, s) -> m.put(s, true), HashMap::putAll);

            // Find available service operators at the requested time
            for(var operator : busyServiceOperators) {
                allOperators.put(operator.getName(), false);
            }

            String availabelOperatorname = "";

            // Find the first available service operator
            for (var entry : allOperators.entrySet()) {
                if(entry.getValue()){
                    availabelOperatorname = entry.getKey();
                    break;
                }
            }
            if(!availabelOperatorname.isEmpty()){
                // If an available service operator is found, book the appointment
                ServiceOperator serviceOperator1 = serviceOperatorRepository.findByName(availabelOperatorname)
                        .orElseThrow(() -> new CustomException("Invalid service operator name", "INVALID_SERVICE_OPERATOR"));


                appointment.setStartHour(newStartHour);
                appointment.setEndHour(newEndHour);
                appointment.setServiceOperator(serviceOperator1);

                appointmentRepository.save(appointment);

                return AppointmentResponseDTO.builder()
                        .id(appointment.getId())
                        .startHour(appointment.getStartHour())
                        .endHour(appointment.getEndHour())
                        .serviceOperatorName(appointment.getServiceOperator().getName())
                        .build();
            }
            else{
                throw new CustomException("All service operators are busy at this time. Please choose another time.", "ALL_OPERATORS_BUSY");
            }
        }
        else{
            // If no service operators are busy, book the appointment with the default service operator
            ServiceOperator defaultServiceOperator = getDefaultServiceOperator();
            appointment.setStartHour(newStartHour);
            appointment.setEndHour(newEndHour);
            appointment.setServiceOperator(defaultServiceOperator);

            appointmentRepository.save(appointment);

            return AppointmentResponseDTO.builder()
                    .id(appointment.getId())
                    .startHour(appointment.getStartHour())
                    .endHour(appointment.getEndHour())
                    .serviceOperatorName(appointment.getServiceOperator().getName())
                    .build();
        }
    }

    public void cancelAppointment(Long appointmentId) {
        // Find the appointment corresponding to the given ID and delete it
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new CustomException("No Appointment found with appointment ID", "INVALID_APPOINTMENT_ID"));
        appointmentRepository.deleteById(appointmentId);
    }

    public List<AppointmentResponseDTO> getBookedAppointmentsForOperator(String serviceOperatorName) {
        // Find the service operator corresponding to the given name
        ServiceOperator serviceOperator = serviceOperatorRepository.findByName(serviceOperatorName)
                .orElseThrow(() -> new CustomException("Invalid service operator name", "INVALID_SERVICE_OPERATOR"));

        // Get the list of appointments for the service operator in sorted order based on start hour
        // Convert the list of appointments to a list of AppointmentResponseDTO objects
        List<AppointmentResponseDTO> appointments = appointmentRepository.findByServiceOperatorOrderByStartHourAsc(serviceOperator).stream().map(appointment -> {
            return AppointmentResponseDTO.builder()
                    .id(appointment.getId())
                    .startHour(appointment.getStartHour())
                    .endHour(appointment.getEndHour())
                    .serviceOperatorName(appointment.getServiceOperator().getName())
                    .build();
        }).toList();

        return appointments;
    }

    public List<TimeSlot> getAvailableTimeSlotsForOperator(String serviceOperatorName) {
        // Find the service operator corresponding to the given name
        ServiceOperator serviceOperator = serviceOperatorRepository.findByName(serviceOperatorName)
                .orElseThrow(() -> new CustomException("Invalid service operator name", "INVALID_SERVICE_OPERATOR"));

        // Get the list of appointments for the service operator in sorted order based on start hour
        List<Appointment> appointments = appointmentRepository.findByServiceOperatorOrderByStartHourAsc(serviceOperator);

        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        int startHour = 0;
        int endHour = 24;

        // Find available time slots based on the appointments
        for (Appointment appointment : appointments) {
            if (appointment.getStartHour() > startHour) {
                availableTimeSlots.add(new TimeSlot(startHour, appointment.getStartHour()));
            }
            startHour = appointment.getEndHour();
        }

        // Add the remaining time slots if there are any
        if (startHour < endHour) {
            availableTimeSlots.add(new TimeSlot(startHour, endHour));
        }
        return availableTimeSlots;
    }

    private ServiceOperator getDefaultServiceOperator() {
        // If there is no default service operator, create one
        return serviceOperatorRepository.findByName("ServiceOperator0")
                .orElseGet(() -> {
                    ServiceOperator defaultOperator = new ServiceOperator();
                    defaultOperator.setName("ServiceOperator0");
                    return serviceOperatorRepository.save(defaultOperator);
                });
    }
}
