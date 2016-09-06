package se.jaitco.queueticketapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

//@Value
//@Builder
//@JsonDeserialize(builder = Ticket.TicketBuilder.class)
@Data
public class Ticket {

    long number;

    private long time;

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
