package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Test;
import se.jaitco.queueticketapi.model.User;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

public class UserServiceTest {

    private final UserService classUnderTest = new UserService();

    @Test
    public void testGetUser() {
        Optional<User> user = classUnderTest.getUser("Aschan");

        Assert.assertThat(user.isPresent(), is(true));
    }

}