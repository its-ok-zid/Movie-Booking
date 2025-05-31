package com.tech.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {

    @NotBlank(message = "Movie name is mandatory")
    private String movieName;

    @NotBlank(message = "Theatre name is mandatory")
    private String theatreName;

    @Min(value = 1, message = "Number of tickets must be at least 1")
    private int numberOfTickets;

    @NotBlank(message = "Seat numbers are mandatory")
    private String seatNumbers;
}
