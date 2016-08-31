package se.jaitco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.jaitco.model.Ticket;
import se.jaitco.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public Ticket takeTicket() {
        return Ticket.builder()
                .info(ticketService.takeTicket())
                .build();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public void resetTickets() {
        ticketService.resetTickets();
    }

}