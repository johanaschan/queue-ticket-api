package se.jaitco.queueticketapi.model;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
public class TicketNumber {

    @Min(0)
    @NotNull
    long number;

    public TicketNumber(String number) {
        this.number = Long.parseLong(number);
    }

}
