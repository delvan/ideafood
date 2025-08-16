package com.ideaprog.ideafood.delivery.tracking;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.ideaprog.ideafood.delivery.tracking.domain.exception.DomainException;
import com.ideaprog.ideafood.delivery.tracking.domain.model.ContactPoint;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery;
import com.ideaprog.ideafood.delivery.tracking.domain.model.Delivery.PreparationDetails;
import com.ideaprog.ideafood.delivery.tracking.domain.model.DeliveryStatus;


class DeliveryTrackingApplicationTests {

	@Test
	public void shouldChangeToPlace() {
		Delivery delivery = Delivery.draft();
		delivery.editPreparationDetails(cretedValidPreparationDetails());

		delivery.place();
		assert delivery.getStatus() == DeliveryStatus.WAITING_FOR_COURIER;
		assertNotNull(delivery.getPlacedAt());
	}

	@Test
	public void shouldNotPlace() {
		Delivery delivery = Delivery.draft();

		assertThrows(DomainException.class, () -> {
			delivery.place();
		});

		assert delivery.getStatus() == DeliveryStatus.DRAFT;
		assertNull(delivery.getPlacedAt());
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
