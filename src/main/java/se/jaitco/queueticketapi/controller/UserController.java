package se.jaitco.queueticketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.jaitco.queueticketapi.model.LoginRequest;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.service.AuthenticationService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(LoginRequest login) {
        return authenticationService.login(login);
    }

}
