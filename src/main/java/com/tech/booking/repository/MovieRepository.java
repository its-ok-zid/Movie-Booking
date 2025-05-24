package com.tech.booking.repository;

import com.tech.booking.model.Movie;
import com.tech.booking.model.MovieId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, MovieId> {
}
