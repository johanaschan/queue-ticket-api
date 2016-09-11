package se.jaitco.queueticketapi.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketStatus {

    long numbersBefore;

    long estimatedWaitTime;

}