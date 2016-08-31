package se.jaitco.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = se.jaitco.model.Ticket.TicketBuilder.class)
public class Ticket {

    int number;

    long time;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TicketBuilder {
    }
}
