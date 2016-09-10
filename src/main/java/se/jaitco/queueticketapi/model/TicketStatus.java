
package se.jaitco.queueticketapi.model;

import lombok.Value;

import javax.validation.constraints.Min;

@Value
public class TicketStatus {

    long numbersBefore;

    long estimatedWaitTime;

}