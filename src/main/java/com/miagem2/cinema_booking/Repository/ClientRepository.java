package com.miagem2.cinema_booking.Repository;
import com.miagem2.cinema_booking.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

}
