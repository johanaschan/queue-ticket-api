package se.jaitco.queueticketapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude={"time"})
public class Ticket {

    private long time;

    private long number;


}
