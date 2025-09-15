package com.dexterv.eventticket.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequestDto {
    @NotBlank(message = "Ticket Type name is required")
    private String name;

    @NotNull(message="Price is required")
    @PositiveOrZero(message = "Price must be zero or greater")
    private Double price;

    private String description;
    private Integer totalAvailable;
}
