package com.ideaprog.ideafood.delivery.tracking;

import org.junit.jupiter.api.Test;

import com.ideaprog.ideafood.delivery.tracking.domain.model.DeliveryStatus;

public class DeliveryStatusTest {

    @Test
    void canChangeToWaitingForCourier() {
        assert DeliveryStatus.DRAFT.canChangeTo(DeliveryStatus.WAITING_FOR_COURIER);
    }

    @Test
    void canNotChangeToInTransitFromDraft() {
        assert DeliveryStatus.DRAFT.canNotChangeTo(DeliveryStatus.IN_TRANSIT);
    }

}
