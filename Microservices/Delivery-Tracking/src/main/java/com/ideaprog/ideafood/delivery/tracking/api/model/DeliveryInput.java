package com.ideaprog.ideafood.delivery.tracking.api.model;


import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryInput {

    @NotNull
    @Valid
    private ContactPointInput sender;

    @NotNull
    @Valid
    private ContactPointInput recipient;


    @NotEmpty
    @Valid
    @Size(min = 1)
    private List<ItemInput> items;
 
}
