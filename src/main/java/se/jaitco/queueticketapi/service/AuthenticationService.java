package se.jaitco.queueticketapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import se.jaitco.queueticketapi.model.LoginRequest;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.User;

import java.util.Optional;

@Component
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextHolderService securityContextHolderService;

    @Autowired
    private JwtTokenService jwtTokenUtil;

    public LoginResponse login(LoginRequest login) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getName(),
                        login.getPassword()
                )
        );
        securityContextHolderService.setAuthentication(authentication);
        Optional<User> user = userService.getUser(login.getName());
        final String token = jwtTokenUtil.generateToken(jwtTokenUtil.buildJWTUser(user.get()));
        return LoginResponse.builder().token(token).build();
    }

}
