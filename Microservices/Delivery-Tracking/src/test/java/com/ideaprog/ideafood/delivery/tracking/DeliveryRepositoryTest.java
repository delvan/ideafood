package com.ideaprog.ideafood.delivery.tracking;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ideaprog.ideafood.delivery.tracking.domain.model.ContactPoint;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery.PreparationDetails;
import com.ideaprog.ideafood.delivery.tracking.domain.repository.DeliveryRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist() {

        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(cretedValidPreparationDetails());

        delivery.addItem("Computado", 10);
        delivery.addItem("Mouse", 1);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persitedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persitedDelivery.getItems().size());

    }

    private PreparationDetails cretedValidPreparationDetails() {

        ContactPoint sender = ContactPoint.builder()
                .zipCode("12345-678")
                .street("Main St")
                .number("123")
                .complement("Apt 4B")
                .name("John Doe")
                .phone("123-456-7890")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("98765-432")
                .street("Second St")
                .number("456")
                .complement("Suite 5A")
                .name("Jane Smith")
                .phone("987-654-3210")
                .build();

        return PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal("10.00"))
                .courierPayout(new BigDecimal("5.00"))
                .expectedDeliveryTime(Duration.ofHours(1))
                .build();

    }

}
