package se.jaitco.queueticketapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jaitco.queueticketapi.model.User;
import se.jaitco.queueticketapi.model.Roles;
import se.jaitco.queueticketapi.model.UserDetailsImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userResponse = userService.getUser(username);
        User user = userResponse.orElseThrow(()-> new UsernameNotFoundException(String.format("user not found '%s'.", username)));
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        return UserDetailsImpl.builder().username(user.getUsername()).password(user.getPassword()).authorities(mapToGrantedAuthorities(user.getGrantedRoles())).build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Roles> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    };

}
