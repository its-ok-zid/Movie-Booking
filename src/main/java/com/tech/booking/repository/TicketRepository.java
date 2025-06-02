package com.tech.booking.repository;

import com.tech.booking.model.MovieId;
import com.tech.booking.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT COALESCE(SUM(t.numberOfTickets), 0) FROM Ticket t WHERE t.movieId = :movieId")
    int sumTicketsByMovieId(@Param("movieId") MovieId movieId);


    boolean existsByMovieId(MovieId movieId);
}

