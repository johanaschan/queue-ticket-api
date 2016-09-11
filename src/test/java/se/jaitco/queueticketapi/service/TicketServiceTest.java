package se.jaitco.queueticketapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketTime;

import static se.jaitco.queueticketapi.service.TicketService.TICKET_LOCK;
import static se.jaitco.queueticketapi.service.TicketService.TICKET_QUEUE;
import static se.jaitco.queueticketapi.service.TicketService.TICKET_TIMES;

public class TicketServiceTest {

    @InjectMocks
    private final TicketService classUnderTest = new TicketService();

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @Mock
    private RDeque<Object> tickets;

    @Mock
    private RDeque<Object> ticketTimes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(redissonClient.getLock(TICKET_LOCK))
                .thenReturn(rLock);
        Mockito.when(redissonClient.getDeque(TICKET_QUEUE))
                .thenReturn(tickets);
        Mockito.when(redissonClient.getDeque(TICKET_TIMES))
                .thenReturn(ticketTimes);
        Mockito.when(tickets.poll())
                .thenReturn(ticket());
    }

    @Test
    public void testNewTicket() {
    }

    @Test
    public void testResetTickets() {

    }

    @Test
    public void testNextTicket() {
        classUnderTest.nextTicket();

        Mockito.verify(redissonClient, Mockito.times(1)).getLock(Matchers.anyString());
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(redissonClient, Mockito.times(2)).getDeque(Matchers.anyString());
        Mockito.verify(tickets, Mockito.times(1)).poll();
        Mockito.verify(ticketTimes, Mockito.times(1)).add(Matchers.any(TicketTime.class));
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    @Test
    public void testCurrentTicket() {

    }

    @Test
    public void testGetTicketStatus() {

    }

    private Ticket ticket() {
        Ticket ticket = new Ticket();
        ticket.setNumber(1);
        ticket.setTime(System.nanoTime());
        return ticket;
    }

}