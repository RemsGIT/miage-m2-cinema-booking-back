package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.EntityRequest.MovieRequest;
import com.miagem2.cinema_booking.Model.Category;
import com.miagem2.cinema_booking.Model.Movie;
import com.miagem2.cinema_booking.Repository.CategoryRepository;
import com.miagem2.cinema_booking.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Movie> getAllMovies(@RequestParam(name = "last", required = false) String last) {
        return (last != null && last.equals("true")) ? movieRepository.findFirst10ByOrderByIdDesc() : movieRepository.findAll();
    }

    @PostMapping
    public Movie createMovie(@RequestBody MovieRequest movieRequest) {
        if (movieRequest.getCategoryIds() != null && !movieRequest.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(movieRequest.getCategoryIds()));

            movieRequest.getMovie().setCategories(categories);
        }

        Movie savedMovie = movieRepository.save(movieRequest.getMovie());

        return savedMovie;
    }

    @GetMapping("/{movieId}")
    public Movie getMovieById(@PathVariable Long movieId) {
        return movieRepository.findById(movieId).orElse(null);
    }

    @GetMapping("/{movieId}/sessions")
    public ResponseEntity<?> getSessions(@PathVariable Long movieId) {
        Optional<Movie> movie =  movieRepository.findById(movieId);

        if (movie.isPresent()) {
            // Build object response => get session details on movie
            List<Map<String, Object>> sessions = movie.get().getSessions().stream()
                    .map(session -> {
                        Map<String, Object> sessionMap = new HashMap<>();
                        sessionMap.put("id", session.getId());
                        sessionMap.put("date", session.getDate());
                        sessionMap.put("type", session.getType());
                        sessionMap.put("room", session.getRoom());

                        return sessionMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("movie", Map.of("id", movie.get().getId(), "name", movie.get().getName()));
            responseMap.put("sessions", sessions);

            return ResponseEntity.ok(responseMap);
        }

        return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{movieId}")
    public Movie updateMovie(@PathVariable Long movieId, @RequestBody MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if (movie != null) {
            movie.setName(movieRequest.getMovie().getName());
            movie.setDescription(movieRequest.getMovie().getDescription());
            movie.setRelease(movieRequest.getMovie().getRelease());
            movie.setDuration(movieRequest.getMovie().getDuration());
            movie.setImage(movieRequest.getMovie().getImage());

            // Update movie's categories
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(movieRequest.getCategoryIds()));
            movie.setCategories(categories);

            return movieRepository.save(movie);
        }

        return null;
    }

    @DeleteMapping("/{movieId}")
    public void deleteMovie(@PathVariable Long movieId) {
        movieRepository.deleteById(movieId);
    }

    @GetMapping("/search")
    public List<Movie> searchMovies(@RequestParam(name = "name") String name) {
        return movieRepository.findByNameContainingIgnoreCase(name);
    }
}
