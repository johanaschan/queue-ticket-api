package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import se.jaitco.queueticketapi.model.Roles;
import se.jaitco.queueticketapi.model.User;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private final UserDetailsServiceImpl classUnderTest = new UserDetailsServiceImpl();

    @Mock
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername() {
        final String userName = "USERNAME";
        Mockito.when(userService.getUser(Matchers.anyString()))
                .thenReturn(Optional.of(User.builder()
                        .username(userName)
                        .password("Fotboll")
                        .grantedRoles(Arrays.asList(Roles.CUSTOMER))
                        .build()));
        UserDetails userDetails = classUnderTest.loadUserByUsername(userName);

        Assert.assertThat(userDetails.getUsername(), is(userName));

        Mockito.verify(userService, Mockito.times(1))
                .getUser(userName);
    }

}