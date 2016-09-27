package se.jaitco.queueticketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public Ticket newTicket() {
        return ticketService.newTicket();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/drop/{ticketNumber}", method = RequestMethod.DELETE)
    public void dropTicket(@PathVariable("ticketNumber") long ticketNumber) {
        ticketService.dropTicket(ticketNumber);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/reset", method = {RequestMethod.DELETE})
    public void resetTickets() {
        ticketService.resetTickets();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/next", method = {RequestMethod.POST})
    public void nextTicket() {
        ticketService.nextTicket();
    }

    @RequestMapping(value = "/current", method = {RequestMethod.GET})
    public Ticket currentTicket() {
        return ticketService.currentTicket().orElseThrow(NotFoundException::new);
    }

    @RequestMapping(value = "/ticketstatus/{ticketNumber}", method = {RequestMethod.GET})
    public TicketStatus ticketStatus(@PathVariable("ticketNumber") long ticketNumber) {
        return ticketService.ticketStatus(ticketNumber).orElseThrow(NotFoundException::new);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected static class NotFoundException extends RuntimeException{
    }
}

