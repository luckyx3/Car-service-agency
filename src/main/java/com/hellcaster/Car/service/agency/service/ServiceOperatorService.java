package com.hellcaster.Car.service.agency.service;

import com.hellcaster.Car.service.agency.entities.ServiceOperator;
import com.hellcaster.Car.service.agency.repository.ServiceOperatorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceOperatorService {

    private final ServiceOperatorRepository serviceOperatorRepository;

    // Create service operators if they do not exist in the database
    @PostConstruct
    public void initializeServiceOperators() {
        createServiceOperatorIfNotExists("ServiceOperator0");
        createServiceOperatorIfNotExists("ServiceOperator1");
        createServiceOperatorIfNotExists("ServiceOperator2");
    }

    private void createServiceOperatorIfNotExists(String name) {
        if (!serviceOperatorRepository.existsByName(name)) {
            ServiceOperator serviceOperator = ServiceOperator.builder()
                    .name(name)
                    .build();
            serviceOperatorRepository.save(serviceOperator);
        }
    }
}
