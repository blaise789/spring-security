package com.codedailywithme.springSecurityPractice.service;

import com.codedailywithme.springSecurityPractice.auditing.ApplicationAuditingAware;
import com.codedailywithme.springSecurityPractice.config.JwtService;
import com.codedailywithme.springSecurityPractice.dto.AuthenticationRequest;
import com.codedailywithme.springSecurityPractice.dto.AuthenticationResponse;
import com.codedailywithme.springSecurityPractice.dto.RegisterRequest;
import com.codedailywithme.springSecurityPractice.model.Token;
import com.codedailywithme.springSecurityPractice.repository.TokenRepository;
import com.codedailywithme.springSecurityPractice.enums.TokenType;
import com.codedailywithme.springSecurityPractice.model.Role;
import com.codedailywithme.springSecurityPractice.model.User;
import com.codedailywithme.springSecurityPractice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticateService {
    private  final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private  final JwtService jwtService;
    private  final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
     if(userRepository.existsByEmail(request.getEmail())){
         return new AuthenticationResponse(null,null,"user already exists");
     }

         User user = User.builder()
                 .firstName(request.getFirstName())
                 .lastName(request.getLastName())
                 .email(request.getEmail())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .role(Role.USER)
                 .build();

         var savedUser = userRepository.save(user);
         String jwtToken = jwtService.generateToken(user);
         String refreshToken = jwtService.generateRefreshToken(user);
         saveUserToken(savedUser, jwtToken);


         return AuthenticationResponse.builder()
                 .accessToken(jwtToken)
                 .refreshToken(refreshToken)
                 .message("user created successfully")
                 .build();

     }



    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
     Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
     if(authentication.isAuthenticated()){
             User user=userRepository.findByEmail(authRequest.getEmail()).orElseThrow( );
             String jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();}
     else {
         return AuthenticationResponse.builder().accessToken(null).message("login failed").build();
     }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws
            IOException {
   final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
   final String refreshToken;
   final String userEmail;
   if(authHeader==null || !authHeader.startsWith("Bearer ")){
       return;
   }
   refreshToken=authHeader.substring(7);
   userEmail=jwtService.extractUsername(refreshToken);
   if(userEmail !=null){
       var user=this.userRepository.findByEmail(refreshToken).orElseThrow();
       if(jwtService.isTokenValid(refreshToken,user)){
           var accessToken=jwtService.generateToken(user);
           revokeAllUserTokens(user);
           saveUserToken(user,accessToken);
           var authResponse=AuthenticationResponse.builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build();
           new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
       }
   }
    }
    private void saveUserToken(User user, String jwtToken) {
        Token token=Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    public void revokeAllUserTokens(User user){
        var validUserTokens=tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token-> {
                token.setRevoked(true);
                token.setExpired(true);
            });

        tokenRepository.saveAll(validUserTokens);
    }
}
