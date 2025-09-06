package com.ideaprog.ideafood.courier.management.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ideaprog.ideafood.courier.management.api.model.CourierInput;
import com.ideaprog.ideafood.courier.management.domain.model.Courier;
import com.ideaprog.ideafood.courier.management.domain.repository.CourierRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourierRegistrationService {

    private final CourierRepository courierRepository;

    public Courier create(CourierInput courierInput) {

        Courier courier = Courier.branNew(courierInput.getName(), courierInput.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

    public Courier update(UUID courierId, CourierInput courierInput) {

        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new RuntimeException("Courier not found"));
        courier.setName(courierInput.getName());
        courier.setPhoneNumber(courierInput.getPhone());
        return courierRepository.saveAndFlush(courier);
    }

}
