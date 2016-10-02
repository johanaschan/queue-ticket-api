package se.jaitco.queueticketapi.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import se.jaitco.queueticketapi.model.LoginResponse;
import se.jaitco.queueticketapi.model.UserLogin;
import se.jaitco.queueticketapi.service.TicketService;
import se.jaitco.queueticketapi.service.UserService;

import javax.servlet.ServletException;
import java.util.Optional;

public class UserControllerTest {

    @InjectMocks
    private final UserController classUnderTest = new UserController();

    @Mock
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginCorrect() throws ServletException {
        UserLogin userLogin = UserLogin.builder().build();
        Mockito.when(userService.login(userLogin)).thenReturn(Optional.of(LoginResponse.builder().build()));
        LoginResponse loginResponse = classUnderTest.login(userLogin);
        Mockito.verify(userService, Mockito.times(1)).login(userLogin);
    }

    @Test(expected = UserController.BadRequestException.class)
    public void testLoginFailure() throws ServletException {
        UserLogin userLogin = UserLogin.builder().build();
        Mockito.when(userService.login(userLogin)).thenReturn(Optional.empty());
        classUnderTest.login(userLogin);
    }

}
