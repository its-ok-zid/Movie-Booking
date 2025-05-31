package com.tech.booking.service;

import com.tech.booking.dto.MovieDTO;
import com.tech.booking.dto.TicketRequest;
import com.tech.booking.model.Movie;
import com.tech.booking.model.Ticket;

import java.util.List;

public interface MovieService {

    List<MovieDTO> getAllMovies();
    MovieDTO getMovieByName(String movieName);
    Movie getMovieById(String movieName, String theatreName);
    void deleteMovie(String movieName, String theatreName);
    Ticket bookTicket(TicketRequest request);
    void updateTicketStatus(String movieName, String theatreName, Long ticketId);

}
