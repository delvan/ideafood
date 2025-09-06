package com.ideaprog.ideafood.courier.management.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class AssignedDelivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private OffsetDateTime assignedAt;

    @ManyToOne(optional = false)
    @Getter(AccessLevel.PRIVATE)
    private Courier courier;

    static AssignedDelivery createNew(UUID id, Courier courier) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(id);
        assignedDelivery.setAssignedAt(OffsetDateTime.now());
        assignedDelivery.setCourier(courier);
        return assignedDelivery;
    }

}
