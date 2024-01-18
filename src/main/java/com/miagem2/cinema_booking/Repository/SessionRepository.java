package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> { }
