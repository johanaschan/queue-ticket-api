package se.jaitco.queueticketapi.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.LoginRequest;
import se.jaitco.queueticketapi.service.AuthenticationService;

public class UserControllerTest {

    @InjectMocks
    private final UserController classUnderTest = new UserController();

    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginCorrect(){
        LoginRequest userLogin = LoginRequest.builder().build();
        Mockito.when(authenticationService.login(userLogin)).thenReturn(LoginResponse.builder().build());
        LoginResponse loginResponse = classUnderTest.login(userLogin);

        Mockito.verify(authenticationService, Mockito.times(1)).login(userLogin);
    }

}
