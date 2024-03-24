package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findFirst10ByOrderByIdDesc();

    List<Movie> findByNameContainingIgnoreCase(String name);
}
