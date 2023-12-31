package com.example.security.service;

import com.example.security.entity.Client;
import com.example.security.entity.Token;
import com.example.security.exception.client.ClientJwtNotFoundException;
import com.example.security.exception.client.ClientNotFoundException;
import com.example.security.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public void createToken(Client client, String jwt) {
        tokenRepository.save(
                Token.builder()
                        .client(client)
                        .jwt(jwt)
                        .expired(false)
                        .revoked(false)
                        .build()
        );
    }

    public void deletePreviousClientToken(Client client) {
        tokenRepository.findByClient_Id(client.getId())
                .ifPresent(tokenRepository::delete);
    }

    public boolean isTokenValid(String jwt) {
        UserDetails userDetails = takeUserDetailsFromJwt(jwt);
        return tokenRepository.findByJwt(jwt)
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false) &&
                jwtService.isJwtValid(jwt, userDetails);
    }

    public UserDetails takeUserDetailsFromJwt(String jwt) {
        String email = jwtService.extractEmail(jwt);
        try {
            return userDetailsService.loadUserByUsername(email);
        } catch (ClientNotFoundException e) {
            throw new ClientJwtNotFoundException(e.getMessage());
        }
    }
}
