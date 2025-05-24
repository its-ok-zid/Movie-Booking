package com.tech.booking.model;

import com.tech.booking.enums.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @EmbeddedId
    private MovieId id;

    @Column(name = "total_tickets", nullable = false)
    @Min(value = 1, message = "Total tickets must be at least 1")
    private int totalTickets;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;
}
