package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.EntityRequest.LoginRequest;
import com.miagem2.cinema_booking.EntityRequest.RegisterRequest;
import com.miagem2.cinema_booking.Helpers.JwtTokenHelper;
import com.miagem2.cinema_booking.Model.User;
import com.miagem2.cinema_booking.Service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = JwtTokenHelper.generateToken(userDetails);

            User u = userService.findByEmail(userDetails.getUsername());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("user", Map.of("id", u.getId(), "email", u.getEmail()));
            responseMap.put("jwt", jwtToken);

            return ResponseEntity.ok(responseMap);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email/password combination");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Check if user already exists
        if (Objects.nonNull(userService.findByEmail(registerRequest.getEmail()))) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }

        // Create new user
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

        userService.save(user);

        return ResponseEntity.ok().body("User was created");
    }

}
