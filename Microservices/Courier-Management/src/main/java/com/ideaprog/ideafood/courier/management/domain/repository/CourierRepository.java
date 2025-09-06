package com.ideaprog.ideafood.courier.management.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaprog.ideafood.courier.management.domain.model.Courier;

public interface CourierRepository extends JpaRepository<Courier, UUID> {
    // Additional query methods can be defined here if needed

}
