package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> { }
