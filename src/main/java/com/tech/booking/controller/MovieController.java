package com.tech.booking.controller;

import com.tech.booking.dto.MovieDTO;
import com.tech.booking.dto.TicketRequest;
import com.tech.booking.model.Ticket;
import com.tech.booking.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1.0/moviebooking", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Movie Controller", description = "Handles movie operations including viewing, booking, and updating tickets")
@Slf4j
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "Get all movies")
    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        log.info("Fetching all movies");
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Search movie by name")
    @GetMapping("/movies/search/{movieName}")
    public ResponseEntity<MovieDTO> getMovieByName(@PathVariable String movieName) {
        log.info("Searching for movie: {}", movieName);
        MovieDTO movieDto = movieService.getMovieByName(movieName);
        if (movieDto != null) {
            log.info("Movie found: {}", movieDto.getMovieName());
            return ResponseEntity.ok(movieDto);
        } else {
            log.warn("Movie not found: {}", movieName);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Book tickets for a movie")
    @PostMapping(value = "/{moviename}/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> bookTicket(@PathVariable String moviename,
                                             @Valid @RequestBody TicketRequest request) {
        log.info("Booking ticket for movie: {}", moviename);
        if (!moviename.equalsIgnoreCase(request.getMovieName())) {
            log.warn("Movie name in path '{}' does not match request body '{}'", moviename, request.getMovieName());
            return ResponseEntity.badRequest().build();
        }
        Ticket ticket = movieService.bookTicket(request);
        log.info("Ticket booked successfully for movie: {}", ticket.getMovieId().getMovieName());
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @Operation(summary = "Update ticket status and available ticket count")
    @PutMapping("/{movieName}/update/{ticketId}")
    public ResponseEntity<String> updateTicketStatus(@PathVariable String movieName,
                                                     @PathVariable Long ticketId,
                                                     @RequestParam String theatreName) {
        log.info("Updating ticket status for movie: {}, ticketId: {}", movieName, ticketId);
        movieService.updateTicketStatus(movieName, theatreName, ticketId);
        log.info("Ticket status updated successfully");
        return ResponseEntity.ok("Ticket status updated successfully.");
    }

    @Operation(summary = "Delete movie by name and theatre")
    @DeleteMapping("/{movieName}/delete/{theatreName}")
    public ResponseEntity<String> deleteMovie(@PathVariable String movieName,
                                              @PathVariable String theatreName) {
        log.info("Deleting movie: {} in theatre: {}", movieName, theatreName);
        movieService.deleteMovie(movieName, theatreName);
        log.info("Movie deleted successfully: {} - {}", movieName, theatreName);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}
