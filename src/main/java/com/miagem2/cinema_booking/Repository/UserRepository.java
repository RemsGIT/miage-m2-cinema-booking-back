package com.miagem2.cinema_booking.Repository;

import com.miagem2.cinema_booking.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
