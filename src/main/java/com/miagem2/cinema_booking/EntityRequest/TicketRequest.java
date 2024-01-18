package com.miagem2.cinema_booking.EntityRequest;

import com.miagem2.cinema_booking.Model.Client;
import com.miagem2.cinema_booking.Model.Ticket;

public class TicketRequest {
    private Ticket ticket;

    private Client client;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
