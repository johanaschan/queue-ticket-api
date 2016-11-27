package se.jaitco.queueticketapi.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
@Builder
public class User {

    String username;

    String password;

    Date lastPasswordResetDay;

    @Singular
    List<Roles> grantedRoles;
}
