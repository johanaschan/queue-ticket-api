package se.jaitco.queueticketapi.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
            throw new ServletException("Invalid login");
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
        throw new ServletException("Invalid login");
    }

}
