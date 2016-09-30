package se.jaitco.queueticketapi.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.service.TicketService;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

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
        classUnderTest.resetTickets();

        Mockito.verify(ticketService, Mockito.times(1)).resetTickets();
    }

    @Test
    public void testNextTicket() {
        classUnderTest.nextTicket();

        Mockito.verify(ticketService, Mockito.times(1)).nextTicket();
    }

    @Test
    public void testCurrentTicket() {
        Mockito.when(ticketService.currentTicket()).thenReturn(Optional.of(new Ticket()));
        classUnderTest.currentTicket();

        Mockito.verify(ticketService, Mockito.times(1)).currentTicket();
    }

    @Test
    public void testCurrentTicketNotFound() {
        Mockito.when(ticketService.currentTicket()).thenReturn(Optional.empty());
        Ticket ticket = classUnderTest.currentTicket();
        Assert.assertThat(ticket, is(nullValue()));
    }

    @Test
    public void testTicketStatus() {
        Mockito.when(ticketService.ticketStatus(Matchers.anyLong()))
                .thenReturn(Optional.of(TicketStatus.builder().build()));

        final long ticketNumber = 10L;
        classUnderTest.ticketStatus(ticketNumber);

        Mockito.verify(ticketService, Mockito.times(1)).ticketStatus(ticketNumber);
    }

    @Test
    public void testTicketStatusNotFound() {
        Mockito.when(ticketService.ticketStatus(Matchers.anyLong())).thenReturn(Optional.empty());
        TicketStatus ticketStatus = classUnderTest.ticketStatus(10L);
        Assert.assertThat(ticketStatus, is(nullValue()));
    }

    @Test
    public void testDropTicket() {
        final long ticketNumber = 10L;
        classUnderTest.dropTicket(ticketNumber);

        Mockito.verify(ticketService, Mockito.times(1)).dropTicket(ticketNumber);
    }

    @Test
    public void testSize() {
        classUnderTest.size();

        Mockito.verify(ticketService, Mockito.times(1)).size();
    }

    @Test
    public void testVersion() {
        classUnderTest.version();

        Mockito.verify(ticketService, Mockito.times(1)).version();
    }

}