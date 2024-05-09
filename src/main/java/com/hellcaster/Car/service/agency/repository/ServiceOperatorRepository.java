package com.hellcaster.Car.service.agency.repository;

import com.hellcaster.Car.service.agency.entities.ServiceOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOperatorRepository extends JpaRepository<ServiceOperator, Long> {
    Optional<ServiceOperator> findByName(String name);

    List<ServiceOperator> findAll();

    boolean existsByName(String name);
}
