package com.ideaprog.ideafood.delivery.tracking.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    // Additional query methods can be defined here if needed


}
