package com.dexterv.eventticket.domain.dtos;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeResponseDto {
    private String name;
    private Double price;
    private String description;
    private Integer totalAvailable;
}
