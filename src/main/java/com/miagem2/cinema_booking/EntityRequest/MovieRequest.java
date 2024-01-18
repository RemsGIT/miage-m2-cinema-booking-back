package com.miagem2.cinema_booking.EntityRequest;

import com.miagem2.cinema_booking.Model.Movie;

import java.util.Set;

public class MovieRequest {

    private Movie movie;
    private Set<Long> categoryIds;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
