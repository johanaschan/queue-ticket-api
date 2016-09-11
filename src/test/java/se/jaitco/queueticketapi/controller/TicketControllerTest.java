package se.jaitco.queueticketapi.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import se.jaitco.queueticketapi.model.TicketNumber;
import se.jaitco.queueticketapi.service.TicketService;

import java.util.Optional;

public class TicketControllerTest {

    @InjectMocks
    private final TicketController classUnderTest = new TicketController();

    @Mock
    private TicketService ticketService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNewTicket() {
        classUnderTest.newTicket();

        Mockito.verify(ticketService, Mockito.times(1)).newTicket();
    }

    @Test
    public void testResetTickets() {
        classUnderTest.newTicket();

        Mockito.verify(ticketService, Mockito.times(1)).newTicket();
    }

    @Test
    public void testNextTicket() {
        classUnderTest.nextTicket();

        Mockito.verify(ticketService, Mockito.times(1)).nextTicket();
    }

    @Test
    public void testCurrentTicket() {
        Mockito.when(ticketService.currentTicket()).thenReturn(Optional.empty());

        classUnderTest.currentTicket();

        Mockito.verify(ticketService, Mockito.times(1)).currentTicket();
    }

    @Test
    public void testTicketStatus() {
        Mockito.when(ticketService.getTicketStatus(Matchers.any(TicketNumber.class))).thenReturn(Optional.empty());

        TicketNumber ticketNumber = new TicketNumber("10");
        classUnderTest.ticketStatus(ticketNumber);

        Mockito.verify(ticketService, Mockito.times(1)).getTicketStatus(ticketNumber);
    }

}