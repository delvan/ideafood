package com.ideaprog.ideafood.delivery.tracking.api.controller;

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

import com.ideaprog.ideafood.delivery.tracking.api.model.CourierIdInput;
import com.ideaprog.ideafood.delivery.tracking.api.model.DeliveryInput;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery;
import com.ideaprog.ideafood.delivery.tracking.domain.repository.DeliveryRepository;
import com.ideaprog.ideafood.delivery.tracking.domain.service.DeliveryCheckpointService;
import com.ideaprog.ideafood.delivery.tracking.domain.service.DeliveryPreperationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryPreperationService deliveryPreperationService;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryCheckpointService deliveryCheckpointService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput inputDelivery) {
        // Logic to handle the delivery draft
        return deliveryPreperationService.draft(inputDelivery); // Return the draft delivery for now
    }

    @PutMapping("/{id}")
    public Delivery edit(@RequestBody @Valid DeliveryInput inputDelivery, @PathVariable("id") UUID id) {
        // Logic to handle the delivery draft
        return deliveryPreperationService.edit(id, inputDelivery);// Return the draft delivery for now
    }

    @GetMapping
    public PagedModel<Delivery> listAll(@PageableDefault Pageable pageable) {

        return new PagedModel<>(deliveryRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Delivery getById(@PathVariable UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    @PostMapping("/{id}/placement")
    public void place(@PathVariable UUID id) {

        deliveryCheckpointService.place(id);
    }

    @PostMapping("/{id}/pickups")
    public void pickup(@PathVariable UUID id, @Valid @RequestBody CourierIdInput input) {
        deliveryCheckpointService.pickUp(id, input.getCourierId());

    }

    @PostMapping("/{id}/completion")
    public void completion(@PathVariable UUID id) {
        deliveryCheckpointService.complete(id);

    }

}
