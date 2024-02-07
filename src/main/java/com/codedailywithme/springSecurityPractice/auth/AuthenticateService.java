package com.codedailywithme.springSecurityPractice.auth;

import com.codedailywithme.springSecurityPractice.config.JwtService;
import com.codedailywithme.springSecurityPractice.model.Role;
import com.codedailywithme.springSecurityPractice.model.User;
import com.codedailywithme.springSecurityPractice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
    User user=User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
    userRepository.save(user);
   String jwtToken=jwtService.generateToken(user);
 return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
                User user=userRepository.findByEmail(authRequest.getEmail()).orElseThrow( );
        return AuthenticationResponse.builder().build();
    }
}
