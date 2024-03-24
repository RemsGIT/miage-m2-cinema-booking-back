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
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/{sessionId}")
    public Session getSessionById(@PathVariable Long sessionId) { return sessionRepository.findById(sessionId).orElse(null); }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        Optional<Movie> optionalMovie = movieRepository.findById(session.getMovie().getId());
        Optional<Room> optionalRoom = roomRepository.findById(session.getRoom().getId());
        Optional<Type> optionalType = typeRepository.findById(session.getType().getId());

        if (optionalMovie.isPresent() && optionalRoom.isPresent() && optionalType.isPresent()) {
            session.setMovie(optionalMovie.get());
            session.setRoom(optionalRoom.get());
            session.setType(optionalType.get());

            Session savedSession = sessionRepository.save(session);

            return savedSession;
        }

        return null;
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

    @PutMapping("/{sessionId}")
    public Session updateSession(@PathVariable Long sessionId, @RequestBody Session sessionBody) {
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if(session != null) {
            Optional<Movie> m = movieRepository.findById(sessionBody.getMovie().getId());
            Optional<Room> r = roomRepository.findById(sessionBody.getRoom().getId());
            Optional<Type> t = typeRepository.findById(sessionBody.getType().getId());

            if(m.isPresent() && r.isPresent() && t.isPresent()) {
                session.setDate(sessionBody.getDate());
                session.setMovie(m.get());
                session.setRoom(r.get());
                session.setType(t.get());

                return sessionRepository.save(session);

            }
        }

        return null;
    }
}
