package se.jaitco.queueticketapi.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.UserLogin;

import javax.servlet.ServletException;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody UserLogin login) throws ServletException {
        if (login.getName() == null || login.getPassword() == null) {
            throw new BadRequestException();
        }
        if (login.getName().equals("aschan") && login.getPassword().equals("lmar")) {
            return LoginResponse.builder()
                    .token(Jwts.builder()
                            .setSubject(login.getName())
                            .claim("roles", "tomte")
                            .setIssuedAt(new Date())
                            .signWith(SignatureAlgorithm.HS256, "secretkey")
                            .compact())
                    .build();
        }
        throw new BadRequestException();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected static class BadRequestException extends RuntimeException {
    }

}
