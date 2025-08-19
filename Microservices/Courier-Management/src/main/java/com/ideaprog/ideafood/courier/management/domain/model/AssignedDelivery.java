package com.ideaprog.ideafood.courier.management.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignedDelivery {

    @EqualsAndHashCode.Include
    private UUID id;

    private OffsetDateTime assignedAt;

    static AssignedDelivery createNew(UUID id) {
        AssignedDelivery assignedDelivery = new AssignedDelivery();
        assignedDelivery.setId(id);
        assignedDelivery.setAssignedAt(OffsetDateTime.now());
        return assignedDelivery;
    }

}
