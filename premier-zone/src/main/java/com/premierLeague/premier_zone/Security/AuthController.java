package com.premierLeague.premier_zone.Security;

import ch.qos.logback.classic.encoder.JsonEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepo;

    public AuthController(AuthenticationManager manager, JwtUtility jwtUtility, UserRepository userRepo) {
        this.manager = manager;
        this.jwtUtility = jwtUtility;
        this.userRepo = userRepo;
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        Authentication auth = authenticate(request.username,request.password);
        String token = jwtUtility.generateJwtToken(auth);
        return ResponseEntity.ok("Bearer " + token);
    }


    private Authentication authenticate(String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return manager.authenticate(authenticationToken);
    }
}
