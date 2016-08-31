package se.jaitco.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 * Created by johanaschan on 2016-05-19.
 */
@Value
@Builder
@JsonDeserialize(builder = Ticket.TicketBuilder.class)
public class Ticket {

    String info;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TicketBuilder {
    }
}
