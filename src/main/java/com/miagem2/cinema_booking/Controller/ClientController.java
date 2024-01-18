package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.Model.Client;
import com.miagem2.cinema_booking.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @DeleteMapping("/{clientId}")
    public void deleteClient(@PathVariable Long clientId) {
        clientRepository.deleteById(clientId);
    }
}
