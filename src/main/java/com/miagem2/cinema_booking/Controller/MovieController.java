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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<Movie> getAllMovies(@RequestParam(name = "take", required = false) Integer take) {
        return (take != null && take > 0) ? movieRepository.findTopN(take) : movieRepository.findAll();
    }

    @PostMapping
    public Movie createMovie(@RequestBody MovieRequest movieRequest) {
        if (movieRequest.getCategoryIds() != null && !movieRequest.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(movieRequest.getCategoryIds());

            movieRequest.getMovie().setCategories(categories);
        }

        // Enregistrer le film en base de données
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
    public Movie updateMovie(@PathVariable Long movieId, @RequestBody Movie movieDetails) {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if (movie != null) {
            // Mettez à jour les détails du film
            movie.setName(movieDetails.getName());
            movie.setDescription(movieDetails.getDescription());
            movie.setRelease(movieDetails.getRelease());
            movie.setDuration(movieDetails.getDuration());
            movie.setImage(movieDetails.getImage());

            return movieRepository.save(movie);
        }

        return null;
    }

    @DeleteMapping("/{movieId}")
    public void deleteMovie(@PathVariable Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
