package se.jaitco.queueticketapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class User {

    private String username;
    private String password;
    private Date lastPasswordResetDay;
    private List<Roles> grantedRoles = new LinkedList<Roles>();
}
