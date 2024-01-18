package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.Model.Movie;
import com.miagem2.cinema_booking.Model.Room;
import com.miagem2.cinema_booking.Model.Session;
import com.miagem2.cinema_booking.Model.Type;
import com.miagem2.cinema_booking.Repository.MovieRepository;
import com.miagem2.cinema_booking.Repository.RoomRepository;
import com.miagem2.cinema_booking.Repository.SessionRepository;
import com.miagem2.cinema_booking.Repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TypeRepository typeRepository;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Session session) {
        Optional<Movie> optionalMovie = movieRepository.findById(session.getMovie().getId());
        Optional<Room> optionalRoom = roomRepository.findById(session.getRoom().getId());
        Optional<Type> optionalType = typeRepository.findById(session.getType().getId());

        if (optionalMovie.isPresent() && optionalRoom.isPresent() && optionalType.isPresent()) {
            session.setMovie(optionalMovie.get());
            session.setRoom(optionalRoom.get());
            session.setType(optionalType.get());

            Session savedSession = sessionRepository.save(session);

            String successMessage = "Session created";
            ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("message", successMessage, "session", Map.of("id", savedSession.getId(), "date", savedSession.getDate())));

            return responseEntity;
        } else {
            return new ResponseEntity<>("Certaines des entités associées n'existent pas.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @GetMapping("/{sessionId}/tickets")
    public ResponseEntity<?> getTickets(@PathVariable Long sessionId) {
        Optional<Session> session = sessionRepository.findById(sessionId);

        if(session.isPresent()) {
            List<Map<String, Object>> tickets = session.get().getTickets().stream()
                    .map(ticket -> {
                        Map<String, Object> ticketMap = new HashMap<>();

                        ticketMap.put("id", ticket.getId());
                        ticketMap.put("amount", ticket.getAmount());
                        ticketMap.put("client", ticket.getClient());
                        ticketMap.put("seat", ticket.getSeat());
                        ticketMap.put("date", ticket.getCreatedAt());

                        return ticketMap;
                    })
                    .toList();

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("session", Map.of("id", session.get().getId(), "date", session.get().getDate()));
            responseMap.put("tickets", tickets);

            return ResponseEntity.ok(responseMap);
        }
        return new ResponseEntity<>("Session not found", HttpStatus.NOT_FOUND);
    }
}
