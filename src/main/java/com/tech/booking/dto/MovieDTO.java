package com.tech.booking.dto;

import com.tech.booking.enums.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    @NotBlank(message = "Movie name is mandatory")
    private String movieName;

    @NotBlank(message = "Theatre name is mandatory")
    private String theatreName;

    @Min(value = 1, message = "Total tickets must be at least 1")
    private int totalTickets;

    private TicketStatus status;

}
