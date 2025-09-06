package com.ideaprog.ideafood.delivery.tracking.domain.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideaprog.ideafood.delivery.tracking.api.model.ContactPointInput;
import com.ideaprog.ideafood.delivery.tracking.api.model.DeliveryInput;
import com.ideaprog.ideafood.delivery.tracking.domain.exception.DomainException;
import com.ideaprog.ideafood.delivery.tracking.domain.model.ContactPoint;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery;
import com.ideaprog.ideafood.delivery.tracking.domain.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryPreperationService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput deliveryInput) {

        Delivery delivery = Delivery.draft();
        handlePreparation(deliveryInput, delivery);
        // Business logic for drafting a delivery
        return deliveryRepository.saveAndFlush(delivery);

    }

    @Transactional
    public Delivery edit(UUID id, DeliveryInput deliveryInput) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new DomainException("Delivery not found"));
        delivery.clearItems();
        handlePreparation(deliveryInput, delivery);

        // Business logic for editing a delivery
        return deliveryRepository.saveAndFlush(delivery);

    }

    private void handlePreparation(DeliveryInput deliveryInput, Delivery delivery) {
        ContactPointInput inputSender = deliveryInput.getSender();
        ContactPointInput inputRecipient = deliveryInput.getRecipient();

        ContactPoint sender = ContactPoint.builder()
                .zipCode(inputSender.getZipCode())
                .street(inputSender.getStreet())
                .number(inputSender.getNumber())
                .complement(inputSender.getComplement())
                .name(inputSender.getName())
                .phone(inputSender.getPhone())
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode(inputRecipient.getZipCode())
                .street(inputRecipient.getStreet())
                .number(inputRecipient.getNumber())
                .complement(inputRecipient.getComplement())
                .name(inputRecipient.getName())
                .phone(inputRecipient.getPhone())
                .build();

        Duration expectedDeliveryTime = Duration.ofHours(3); // Example duration

        BigDecimal payout = BigDecimal.valueOf(10); // Example payout

        BigDecimal distanceFee = new BigDecimal(10);

        var preparationDetails = Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .expectedDeliveryTime(expectedDeliveryTime)
                .courierPayout(payout)
                .distanceFee(distanceFee)
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (var itemInput : deliveryInput.getItems()) {
            delivery.addItem(itemInput.getName(), itemInput.getQuantity());

        }

    }

}
