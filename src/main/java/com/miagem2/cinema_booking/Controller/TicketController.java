package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.EntityRequest.TicketRequest;
import com.miagem2.cinema_booking.Model.Client;
import com.miagem2.cinema_booking.Model.Ticket;
import com.miagem2.cinema_booking.Repository.ClientRepository;
import com.miagem2.cinema_booking.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest ticketRequest) {
        Ticket ticket = ticketRequest.getTicket();
        Client client = ticketRequest.getClient();

        if(Objects.nonNull(ticket) && Objects.nonNull(client)) {
            if (client.getEmail() != null && !client.getEmail().isEmpty()) {
                Optional<Client> optionalClient = clientRepository.findByEmail(client.getEmail());

                if (optionalClient.isPresent()) {
                    // If client exists, link to him
                    ticket.setClient(optionalClient.get());
                } else {
                    // If client doesn't exist, create a new and link after
                    clientRepository.save(client);
                    ticket.setClient(client);
                }
                ticketRepository.save(ticket);
                return ResponseEntity.ok(Map.of("success", true, "ticket", ticket));

            }
        }

        return new ResponseEntity<>("Ticket & client information are required.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{ticketId}")
    public void deleteTicket(@PathVariable Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
