package se.jaitco.queueticketapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

//@Value
//@Builder
//@JsonDeserialize(builder = Ticket.TicketBuilder.class)
public class Ticket {

    @Getter @Setter private long number;

    @Getter @Setter private long time;

    public Ticket(){

    }

    public Ticket(long number, long time){
        this.number = number;
        this.time = time;
    }



//    @JsonPOJOBuilder(withPrefix = "")
//    public static final class TicketBuilder {
//    }
}
