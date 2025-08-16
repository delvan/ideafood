package com.ideaprog.ideafood.delivery.tracking.domain.model;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Item {

    @EqualsAndHashCode.Include
    private UUID id;
    private String name;
    
    @Setter(AccessLevel.PACKAGE)
    private Integer quantity;

    
     static Item brandNew( String name, Integer quantity) {
        Item item = new Item();
        item.setId(UUID.randomUUID());
        item.setName(name);;
        item.setQuantity(0);
        return item;
    }

}
