package se.jaitco.queueticketapi.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.UserLogin;
import se.jaitco.queueticketapi.service.TicketService;
import se.jaitco.queueticketapi.service.UserService;

import javax.servlet.ServletException;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody UserLogin login) throws ServletException {
        return userService.login(login).orElseThrow(BadRequestException::new);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected static class BadRequestException extends RuntimeException {
    }

}
