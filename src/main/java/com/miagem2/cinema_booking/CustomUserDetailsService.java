package com.miagem2.cinema_booking;

import com.miagem2.cinema_booking.Model.User;
import com.miagem2.cinema_booking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);


        if (user != null) {

            Collection<SimpleGrantedAuthority> roles = new HashSet<>();
            roles.add(new SimpleGrantedAuthority(user.getRole().toString()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(), roles);
        } else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }
}
