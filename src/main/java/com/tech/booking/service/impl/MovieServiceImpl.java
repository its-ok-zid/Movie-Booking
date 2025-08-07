package com.tech.booking.service.impl;

import com.tech.booking.dto.MovieDTO;
import com.tech.booking.dto.TicketRequest;
import com.tech.booking.enums.TicketStatus;
import com.tech.booking.exception.ResourceNotFoundException;
import com.tech.booking.model.Movie;
import com.tech.booking.model.MovieId;
import com.tech.booking.model.Ticket;
import com.tech.booking.repository.MovieRepository;
import com.tech.booking.repository.TicketRepository;
import com.tech.booking.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;

    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> new MovieDTO(
                        movie.getId().getMovieName(),
                        movie.getId().getTheatreName(),
                        movie.getTotalTickets(),
                        movie.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getMovieByName(String movieName) {
        Optional<Movie> result = movieRepository.findAll().stream()
                .filter(movie -> movie.getId().getMovieName().equalsIgnoreCase(movieName))
                .findFirst();

        return result.map(movie -> new MovieDTO(
                        movie.getId().getMovieName(),
                        movie.getId().getTheatreName(),
                        movie.getTotalTickets(),
                        movie.getStatus()))
                .orElseThrow(() -> new RuntimeException("Movie not found with name: " + movieName));


    }

    @Override
    public Movie getMovieById(String movieName, String theatreName) {
        return movieRepository.findById(new MovieId(movieName, theatreName))
                .orElseThrow(() -> new RuntimeException("Movie not found with name: " + movieName + " and theatre: " + theatreName));
    }

    @Override
    public void deleteMovie(String movieName, String theatreName) {
        movieRepository.deleteById(new MovieId(movieName, theatreName));
    }

    @Override
    public Ticket bookTicket(TicketRequest request) {
        MovieId movieId = new MovieId(request.getMovieName(), request.getTheatreName());
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found for booking"));

        int available = movie.getTotalTickets();

        if (request.getNumberOfTickets() > available) {
            throw new IllegalArgumentException("Not enough tickets available");
        }

        Ticket ticket = new Ticket();
        ticket.setMovieId(movieId);
        ticket.setNumberOfTickets(request.getNumberOfTickets());
        ticket.setSeatNumber(request.getSeatNumbers());

        Ticket saved = ticketRepository.save(ticket);

        int updatedRemaining = available - request.getNumberOfTickets();
        movie.setTotalTickets(updatedRemaining);
        movie.setStatus(updatedRemaining == 0 ? TicketStatus.SOLD_OUT : TicketStatus.BOOK_ASAP);
        movieRepository.save(movie);

        return saved;
    }

   @Override
    public void updateTicketStatus(String movieName, String theatreName, MovieDTO request) {
        MovieId movieId = new MovieId(movieName, theatreName);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found."));

        // Update totalTickets
        movie.setTotalTickets(request.getTotalTickets());

        // Set status based on totalTickets
        if (request.getTotalTickets() > 0) {
            movie.setStatus(TicketStatus.BOOK_ASAP);
        } else {
            movie.setStatus(TicketStatus.SOLD_OUT);
        }

        movieRepository.save(movie);
    }

    @Override
    public boolean hasBookings(String movieName, String theatreName) {
        MovieId movieId = new MovieId(movieName, theatreName);
        return ticketRepository.existsByMovieId(movieId);
    }

    @Override
    public List<String> getBookedSeats(String movieName, String theatreName) {
        MovieId movieId = new MovieId(movieName, theatreName);
        List<Ticket> tickets = ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getId().equals(movieId))
                .collect(Collectors.toList());
        // Handle seatNumber as comma-separated string or single seat
        return tickets.stream()
                .flatMap(ticket -> ticket.getSeatNumber() == null ? java.util.stream.Stream.empty() : java.util.Arrays.stream(ticket.getSeatNumber().split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

}
