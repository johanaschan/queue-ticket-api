package se.jaitco.queueticketapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = LoginRequest.UserLoginBuilder.class)
public class LoginRequest {

    String name;
    
    String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class UserLoginBuilder {
    }
}
