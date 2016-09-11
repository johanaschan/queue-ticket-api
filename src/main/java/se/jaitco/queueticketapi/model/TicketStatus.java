package se.jaitco.queueticketapi.model;

import lombok.Value;

@Value
public class TicketStatus {

    long numbersBefore;

    long estimatedWaitTime;

}