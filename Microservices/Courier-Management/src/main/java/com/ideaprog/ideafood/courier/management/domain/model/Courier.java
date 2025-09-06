package com.ideaprog.ideafood.courier.management.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Entity
public class Courier {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phoneNumber;

    private Integer fulfilledDeliveriesQuantity;
    private Integer pendingDeliveriesQuantity;

    private OffsetDateTime lastFulfiledDeliveryAt;

    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignedDelivery> pendinDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendinDeliveries() {
        return Collections.unmodifiableList(this.pendinDeliveries);
    }

    public static Courier branNew(String name, String phoneNumber) {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhoneNumber(phoneNumber);
        courier.setFulfilledDeliveriesQuantity(0);
        courier.setPendingDeliveriesQuantity(0);
        courier.setLastFulfiledDeliveryAt(OffsetDateTime.now());
        return courier;

    }

    public void assign(UUID deliveryId) {
        this.pendinDeliveries.add(AssignedDelivery.createNew(deliveryId, this));
        this.pendingDeliveriesQuantity++;
    }

    public void fulfill(UUID deliveryId) {
        AssignedDelivery delivery = this.pendinDeliveries.stream().filter(d -> d.getId().equals(deliveryId))
                .findFirst()
                .orElseThrow();

        this.pendinDeliveries.remove(delivery);
        this.pendingDeliveriesQuantity--;
        this.fulfilledDeliveriesQuantity++;
        this.lastFulfiledDeliveryAt = OffsetDateTime.now();
    }

}
