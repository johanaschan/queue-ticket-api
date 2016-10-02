package se.jaitco.queueticketapi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.UserLogin;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

    public Optional<LoginResponse> login(UserLogin login) {
        if ("Aschan".equals(login.getName()) && "Lmar".equals(login.getPassword())) {
            return Optional.of(LoginResponse.builder()
                    .token(Jwts.builder()
                            .setSubject(login.getName())
                            .claim("roles", "tomte")
                            .setIssuedAt(new Date())
                            .signWith(SignatureAlgorithm.HS256, "secretkey")
                            .compact())
                    .build());
        }
        return Optional.empty();
    }
}
