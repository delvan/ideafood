package com.ideaprog.ideafood.delivery.tracking.domain.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.ideaprog.ideafood.delivery.tracking.domain.exception.DomainException;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placedAt;
    private OffsetDateTime assignetAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        ;
        delivery.setDistanceFee(BigDecimal.ZERO);
        // delivery.placedAt = OffsetDateTime.now();

        return delivery;
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity, this);
        items.add(item);
        calculateTotalItems();
        return item.getId();

    }

    public void removeItem(UUID itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void editPreparationDetails(PreparationDetails preparationDetails) {
        verifyIfCanBeEdited();
        this.setSender(preparationDetails.getSender());
        this.setRecipient(preparationDetails.getRecipient());
        this.setDistanceFee(preparationDetails.getDistanceFee());
        this.setCourierPayout(preparationDetails.getCourierPayout());
        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
        calculateTotalItems();

    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException();
        }
    }

    public void clearItems() {
        items.clear();
        calculateTotalItems();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotalItems() {
        int totalItems = getItems().stream()
                .mapToInt(Item::getQuantity)
                .sum();
        setTotalItems(totalItems);
    }

    public void chageItemQuantity(UUID itemId, Integer quantity) {
        Item item = items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow();

        item.setQuantity(quantity);
        calculateTotalItems();

    }

    public void editPreparationDetails(PreparationDetails preparationDetails, BigDecimal totalCost) {
        verifyIfCanBeEdited();
        this.setSender(preparationDetails.getSender());
        this.setRecipient(preparationDetails.getRecipient());
        this.setDistanceFee(preparationDetails.getDistanceFee());
        this.setCourierPayout(preparationDetails.getCourierPayout());
        this.setExpectedDeliveryAt(OffsetDateTime.now().plus(preparationDetails.getExpectedDeliveryTime()));
        setTotalCost(this.getDistanceFee()
                .add(this.getCourierPayout()));
        calculateTotalItems();
    }

    public void place() {
        verify(!items.isEmpty(), "Cannot place a delivery without items");
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        setPlacedAt(OffsetDateTime.now());
    }

    private void verify(boolean b, String string) {
        if (!isFilled()) {
            throw new DomainException();
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException(string);
        }

    }

    private boolean isFilled() {
        return this.getSender() != null && this.getRecipient() != null &&
                this.getTotalCost() != null;
    }

    public void pickUp(UUID courierId) {

        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        setAssignetAt(OffsetDateTime.now());

    }

    public void markAsDelivered() {
        this.changeStatusTo(DeliveryStatus.DELIVERED);
        setFulfilledAt(OffsetDateTime.now());
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;

    }

    private void changeStatusTo(DeliveryStatus newStatus) {

        if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)) {
            throw new DomainException("Cannot change delivery status from " + this.getStatus() + " to " + newStatus);
        }

        this.setStatus(newStatus);
    }

}
