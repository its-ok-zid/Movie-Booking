package com.tech.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.booking.dto.MovieDTO;
import com.tech.booking.dto.TicketRequest;
import com.tech.booking.model.Ticket;
import com.tech.booking.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class TestMovieController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all movies - success")
    void getAllMovies_success() throws Exception {
        MovieDTO movie = new MovieDTO();
        movie.setMovieName("Test Movie");
        List<MovieDTO> movies = Collections.singletonList(movie);

        Mockito.when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/v1.0/moviebooking/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieName").value("Test Movie"));
    }

    @Test
    @DisplayName("Get movie by name - found")
    void getMovieByName_found() throws Exception {
        MovieDTO movie = new MovieDTO();
        movie.setMovieName("Test Movie");

        Mockito.when(movieService.getMovieByName("Test Movie")).thenReturn(movie);

        mockMvc.perform(get("/api/v1.0/moviebooking/movies/search/Test Movie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieName").value("Test Movie"));
    }

    @Test
    @DisplayName("Get movie by name - not found")
    void getMovieByName_notFound() throws Exception {
        Mockito.when(movieService.getMovieByName("Unknown")).thenReturn(null);

        mockMvc.perform(get("/api/v1.0/moviebooking/movies/search/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Book ticket - success")
    void bookTicket_success() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieName("Test Movie");
        movieDTO.setTotalTickets(10);

        TicketRequest request = new TicketRequest();
        request.setMovieName("Test Movie");
        request.setTheatreName("Test Theatre");
        request.setNumberOfTickets(2);
        request.setSeatNumbers("A1,A2");

        // Set up Ticket with non-null movieId
        Ticket ticket = new Ticket();
        com.tech.booking.model.MovieId movieId = new com.tech.booking.model.MovieId();
        movieId.setMovieName("Test Movie");
        movieId.setTheatreName("Test Theatre");
        ticket.setMovieId(movieId);

        Mockito.when(movieService.getMovieByName("Test Movie")).thenReturn(movieDTO);
        Mockito.when(movieService.bookTicket(any(TicketRequest.class))).thenReturn(ticket);

        mockMvc.perform(post("/api/v1.0/moviebooking/Test Movie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Book ticket - movie name mismatch")
    void bookTicket_movieNameMismatch() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieName("Test Movie");
        movieDTO.setTotalTickets(10);

        TicketRequest request = new TicketRequest();
        request.setMovieName("Other Movie");

        Mockito.when(movieService.getMovieByName("Test Movie")).thenReturn(movieDTO);

        mockMvc.perform(post("/api/v1.0/moviebooking/Test Movie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Book ticket - no tickets available")
    void bookTicket_noTicketsAvailable() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieName("Test Movie");
        movieDTO.setTotalTickets(0);

        TicketRequest request = new TicketRequest();
        request.setMovieName("Test Movie");

        Mockito.when(movieService.getMovieByName("Test Movie")).thenReturn(movieDTO);

        mockMvc.perform(post("/api/v1.0/moviebooking/Test Movie/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update ticket status - success")
    void updateTicketStatus_success() throws Exception {
        MovieDTO request = new MovieDTO();

        mockMvc.perform(put("/api/v1.0/moviebooking/Test Movie/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket status updated successfully."));
    }

    @Test
    @DisplayName("Delete movie - no bookings")
    void deleteMovie_noBookings() throws Exception {
        Mockito.when(movieService.hasBookings("Test Movie", "Test Theatre")).thenReturn(false);

        mockMvc.perform(delete("/api/v1.0/moviebooking/Test Movie/delete/Test Theatre"))
                .andExpect(status().isOk())
                .andExpect(content().string("Movie deleted successfully"));
    }

    @Test
    @DisplayName("Delete movie - bookings exist")
    void deleteMovie_bookingsExist() throws Exception {
        Mockito.when(movieService.hasBookings("Test Movie", "Test Theatre")).thenReturn(true);

        mockMvc.perform(delete("/api/v1.0/moviebooking/Test Movie/delete/Test Theatre"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Cannot delete movie 'Test Movie' in theatre 'Test Theatre' as bookings exist for this movie"));
    }
}