package se.jaitco.queueticketapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.jaitco.queueticketapi.model.Roles;
import se.jaitco.queueticketapi.model.User;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class UserService {

    public Optional<User> getUser(String username) {
        Optional<User> user = Optional.empty();
        if ("Aschan".equals(username)) {
            User aschan = User.builder()
                    .username("Aschan")
                    .password("Fotboll")
                    .grantedRoles(Arrays.asList(Roles.CUSTOMER, Roles.ADMIN))
                    .build();
            user = Optional.of(aschan);
        } else if ("Lmar".equals(username)) {
            User lmar = User.builder()
                    .username("Lmar")
                    .password("Book")
                    .grantedRoles(Arrays.asList(Roles.ADMIN))
                    .build();
            user = Optional.of(lmar);
        }
        return user;
    }
}
