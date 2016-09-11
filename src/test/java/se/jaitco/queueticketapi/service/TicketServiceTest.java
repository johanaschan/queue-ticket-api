package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.model.TicketTime;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static se.jaitco.queueticketapi.service.TicketService.*;

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
        Mockito.when(redissonClient.getDeque(TICKETS))
                .thenReturn(tickets);
        Mockito.when(redissonClient.getDeque(TICKET_TIMES))
                .thenReturn(ticketTimes);
        Mockito.when(tickets.poll())
                .thenReturn(ticket());
        Mockito.when(tickets.peek())
                .thenReturn(ticket());
        Mockito.when(tickets.peekLast())
                .thenReturn(ticket());
        Mockito.when(ticketTimes.stream())
                .thenReturn(ticketTimeStream());
        Mockito.when(ticketTimes.size())
                .thenReturn(1);
    }

    @Test
    public void testNewTicket() {
        Ticket ticket = classUnderTest.newTicket();

        Assert.assertThat(ticket.getNumber(), is(2L));
        Mockito.verify(redissonClient, Mockito.times(1)).getLock(TICKET_LOCK);
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(tickets, Mockito.times(1)).add(Matchers.any(Ticket.class));
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    @Test
    public void testResetTickets() {
        classUnderTest.resetTickets();

        Mockito.verify(redissonClient, Mockito.times(1)).getLock(TICKET_LOCK);
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
        Mockito.verify(tickets, Mockito.times(1)).delete();
        Mockito.verify(ticketTimes, Mockito.times(1)).delete();
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    @Test
    public void testNextTicket() {
        classUnderTest.nextTicket();

        Mockito.verify(redissonClient, Mockito.times(1)).getLock(TICKET_LOCK);
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
        Mockito.verify(tickets, Mockito.times(1)).poll();
        Mockito.verify(ticketTimes, Mockito.times(1)).add(Matchers.any(TicketTime.class));
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    @Test
    public void testCurrentTicket() {
        classUnderTest.currentTicket();

        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(tickets, Mockito.times(1)).peek();
    }

    @Test
    public void testTicketStatus() {
        final long ticketNumber = 10L;
        Optional<TicketStatus> ticketStatus = classUnderTest.ticketStatus(ticketNumber);

        Assert.assertThat(ticketStatus.get().getNumbersBefore(), is(9L));
        Mockito.verify(redissonClient, Mockito.times(1)).getLock(TICKET_LOCK);
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(tickets, Mockito.times(1)).peek();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    @Test
    public void testTicketStatusLowNumber() {
        final long ticketNumber = 0L;
        Optional<TicketStatus> ticketStatus = classUnderTest.ticketStatus(ticketNumber);

        Assert.assertThat(ticketStatus, is(Optional.empty()));
    }

    @Test
    public void testTicketStatusSameAsCurrent() {
        final long ticketNumber = 1L;
        Optional<TicketStatus> ticketStatus = classUnderTest.ticketStatus(ticketNumber);

        Assert.assertThat(ticketStatus.get().getEstimatedWaitTime(), is(0L));
    }

    private Ticket ticket() {
        Ticket ticket = new Ticket();
        ticket.setNumber(1);
        ticket.setTime(System.nanoTime());
        return ticket;
    }

    private Stream<Object> ticketTimeStream() {
        TicketTime ticketTime = new TicketTime();
        ticketTime.setDuration(1);
        ticketTime.setTimeStamp(System.nanoTime());
        return Stream.of(ticketTime);
    }

}