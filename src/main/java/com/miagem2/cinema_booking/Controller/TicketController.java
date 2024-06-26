package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.EntityRequest.TicketRequest;
import com.miagem2.cinema_booking.Model.Client;
import com.miagem2.cinema_booking.Model.Ticket;
import com.miagem2.cinema_booking.Repository.ClientRepository;
import com.miagem2.cinema_booking.Repository.TicketRepository;
import com.miagem2.cinema_booking.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping("/sendmail")
    public ResponseEntity<?> sendTicketsByMail(@RequestBody List<Ticket> tickets, MailService mailService) {
        if(!tickets.isEmpty()) {
            mailService.sendEmailWithTickets(tickets.get(0).getClient().getEmail(), tickets);

            return ResponseEntity.ok(Map.of("success", true));
        }
        return ResponseEntity.ok(Map.of("success", false));
    }

    @DeleteMapping("/{ticketId}")
    public void deleteTicket(@PathVariable Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
