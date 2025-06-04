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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMovieServiceImpl {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMovies_shouldReturnListOfMovieDTO() {
        MovieId id = new MovieId("Movie1", "Theatre1");
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTotalTickets(10);
        movie.setStatus(TicketStatus.BOOK_ASAP);

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDTO> result = movieService.getAllMovies();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMovieName()).isEqualTo("Movie1");
        assertThat(result.get(0).getTheatreName()).isEqualTo("Theatre1");
        assertThat(result.get(0).getTotalTickets()).isEqualTo(10);
        assertThat(result.get(0).getStatus()).isEqualTo(TicketStatus.BOOK_ASAP);
    }

    @Test
    void getMovieByName_shouldReturnMovieDTO() {
        MovieId id = new MovieId("Movie1", "Theatre1");
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTotalTickets(5);
        movie.setStatus(TicketStatus.BOOK_ASAP);

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        MovieDTO dto = movieService.getMovieByName("Movie1");

        assertThat(dto.getMovieName()).isEqualTo("Movie1");
        assertThat(dto.getTheatreName()).isEqualTo("Theatre1");
        assertThat(dto.getTotalTickets()).isEqualTo(5);
    }

    @Test
    void getMovieByName_shouldThrowIfNotFound() {
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> movieService.getMovieByName("Unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Movie not found with name: Unknown");
    }


    @Test
    void getMovieById_shouldThrowIfNotFound() {
        MovieId id = new MovieId("Movie1", "Theatre1");
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovieById("Movie1", "Theatre1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Movie not found with name: Movie1 and theatre: Theatre1");
    }



    @Test
    void bookTicket_shouldThrowIfMovieNotFound() {
        MovieId id = new MovieId("Movie1", "Theatre1");
        TicketRequest request = new TicketRequest();
        request.setMovieName("Movie1");
        request.setTheatreName("Theatre1");
        request.setNumberOfTickets(1);

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.bookTicket(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Movie not found for booking");
    }



    @Test
    void updateTicketStatus_shouldThrowIfTicketNotFound() {
        String movieName = "Movie1";
        String theatreName = "Theatre1";
        Long ticketId = 1L;
        MovieDTO dto = new MovieDTO(movieName, theatreName, 0, null);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.updateTicketStatus(movieName, ticketId, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket not found.");
    }

    @Test
    void updateTicketStatus_shouldThrowIfMovieNotFound() {
        String movieName = "Movie1";
        String theatreName = "Theatre1";
        Long ticketId = 1L;
        MovieId id = new MovieId(movieName, theatreName);
        MovieDTO dto = new MovieDTO(movieName, theatreName, 0, null);

        Ticket ticket = new Ticket();
        ticket.setMovieId(id);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.updateTicketStatus(movieName, ticketId, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Movie not found.");
    }


    @Test
    void hasBookings_shouldReturnFalseIfNotExists() {
        MovieId id = new MovieId("Movie1", "Theatre1");
        when(ticketRepository.existsByMovieId(id)).thenReturn(false);
        assertThat(movieService.hasBookings("Movie1", "Theatre1")).isFalse();
    }
}