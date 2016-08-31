package se.jaitco.queueticketapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public Ticket takeTicket() {
        return ticketService.takeTicket();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/reset", method = {RequestMethod.DELETE})
    public void resetTickets() {
        ticketService.resetTickets();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/next", method = {RequestMethod.PATCH})
    public void nextTicket() {
        ticketService.nextTicket();
    }

    @RequestMapping(value = "/current", method = {RequestMethod.GET})
    public Ticket currentTicket() {
        return ticketService.currentTicket().orElse(null);
    }

}