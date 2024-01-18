package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> { }
