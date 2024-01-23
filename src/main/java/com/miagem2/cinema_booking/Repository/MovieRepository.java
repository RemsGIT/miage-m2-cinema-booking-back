package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m ORDER BY m.id DESC")
    List<Movie> findTopN(@Param("n") int n);
}
