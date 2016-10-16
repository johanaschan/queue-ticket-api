package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import se.jaitco.queueticketapi.filter.JwtAuthenticationTokenFilter;
import se.jaitco.queueticketapi.model.LoginRequest;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.User;
import se.jaitco.queueticketapi.model.UserDetailsImpl;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private static String USERNAME = "USERNAME";
    private static String PASSWORD = "PASSWORD";

    @InjectMocks
    private final AuthenticationService classUnderTest = new AuthenticationService();

    @Mock
    private SecurityContextHolderService securityContextHolderService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    UserService userService;

    @Mock
    private JwtTokenService jwtTokenUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private static String TOKEN = "TOKEN";

    @Test
    public void testLogin() throws ServletException, IOException {
        LoginRequest loginRequest = LoginRequest.builder().name(USERNAME).password(PASSWORD).build();
        User user = User.builder().username(USERNAME).password(PASSWORD).build();
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        Optional<User> userOptional = Optional.of(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(null,null,null);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(securityContextHolderService.getAuthentication()).thenReturn(authentication);
        when(userService.getUser(USERNAME)).thenReturn(userOptional);
        when(jwtTokenUtil.buildJWTUser(user)).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(TOKEN);
        LoginResponse loginResponse = classUnderTest.login(loginRequest);
        verify(securityContextHolderService,(times(1))).setAuthentication(authentication);
        Assert.assertEquals(TOKEN,loginResponse.getToken());
    }
}