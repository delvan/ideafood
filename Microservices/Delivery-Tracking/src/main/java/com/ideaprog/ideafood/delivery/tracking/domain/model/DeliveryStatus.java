package com.ideaprog.ideafood.delivery.tracking.domain.model;

import java.util.Arrays;
import java.util.List;

public enum DeliveryStatus {

    DRAFT,
    WAITING_FOR_COURIER(DRAFT),
    IN_TRANSIT(WAITING_FOR_COURIER),
    DELIVERED(IN_TRANSIT);

    private final List<DeliveryStatus> previous;

    DeliveryStatus(DeliveryStatus... previous) {
        this.previous = Arrays.asList(previous);
    }

    public boolean canNotChangeTo(DeliveryStatus next) {
        DeliveryStatus current = this;
        return !next.previous.contains(current);
    }

    public boolean canChangeTo(DeliveryStatus next) {
        return !canNotChangeTo(next);
    }
}
