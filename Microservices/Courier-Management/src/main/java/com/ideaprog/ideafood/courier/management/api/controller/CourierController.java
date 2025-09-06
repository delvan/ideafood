package com.ideaprog.ideafood.courier.management.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ideaprog.ideafood.courier.management.api.model.CourierInput;
import com.ideaprog.ideafood.courier.management.api.service.CourierRegistrationService;
import com.ideaprog.ideafood.courier.management.domain.model.Courier;
import com.ideaprog.ideafood.courier.management.domain.repository.CourierRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierRepository courierRepository;

    private final CourierRegistrationService courierRegistrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Courier create(@Valid @RequestBody CourierInput courierInput) {
        return courierRegistrationService.create(courierInput);

    }

    @PutMapping("/{courierId}")
    public Courier update(@PathVariable UUID courierId, @Valid @RequestBody CourierInput courierInput) {

        return courierRegistrationService.update(courierId, courierInput);

    }

    @GetMapping
    public PagedModel<Courier> listAll(@PageableDefault Pageable pageable) {

        return new PagedModel<Courier>(courierRepository.findAll(pageable));
    }

    @GetMapping("/{courierId}")
    public Courier findById(@PathVariable UUID courierId) {
        return courierRepository.findById(courierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Courier not found"));

    }

}
