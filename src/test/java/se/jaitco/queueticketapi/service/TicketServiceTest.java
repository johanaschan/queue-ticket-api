package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.model.TicketTime;

import java.util.ArrayList;
import java.util.List;
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

    @Mock
    private RAtomicLong ticketNumber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(redissonClient.getLock(TICKET_LOCK))
                .thenReturn(rLock);
        Mockito.when(redissonClient.getDeque(TICKETS))
                .thenReturn(tickets);
        Mockito.when(redissonClient.getDeque(TICKET_TIMES))
                .thenReturn(ticketTimes);
        Mockito.when(redissonClient.getAtomicLong(TICKET_NUMBER))
                .thenReturn(ticketNumber);
        Mockito.when(tickets.poll())
                .thenReturn(ticket());
        Mockito.when(tickets.peek())
                .thenReturn(ticket());
        Mockito.when(tickets.peekLast())
                .thenReturn(ticket());
        Mockito.when(tickets.stream())
                .thenReturn(Stream.of(tickets().toArray()));
        Mockito.when(ticketTimes.stream())
                .thenReturn(Stream.of(ticketTime()));
        Mockito.when(ticketTimes.size())
                .thenReturn(1);
        Mockito.when(ticketNumber.incrementAndGet())
                .thenReturn(2L);
    }

    @Test
    public void testNewTicket() {
        Ticket ticket = classUnderTest.newTicket();

        Assert.assertThat(ticket.getNumber(), is(2L));
        verifyLock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getAtomicLong(TICKET_NUMBER);
        Mockito.verify(tickets, Mockito.times(1)).add(Matchers.any(Ticket.class));
        Mockito.verify(ticketNumber, Mockito.times(1)).incrementAndGet();
    }

    @Test
    public void testResetTickets() {
        classUnderTest.resetTickets();

        verifyLock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
        Mockito.verify(redissonClient, Mockito.times(1)).getAtomicLong(TICKET_NUMBER);
        Mockito.verify(tickets, Mockito.times(1)).delete();
        Mockito.verify(ticketTimes, Mockito.times(1)).delete();
        Mockito.verify(ticketNumber, Mockito.times(1)).set(0L);
    }

    @Test
    public void testNextTicket() {
        classUnderTest.nextTicket();

        verifyLock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
        Mockito.verify(ticketTimes, Mockito.times(1)).add(Matchers.any(TicketTime.class));
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

        verifyLock();
        Assert.assertThat(ticketStatus.get().getNumbersBefore(), is(9L));
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKET_TIMES);
    }

    @Test
    public void testTicketStatusLowNumber() {
        final long ticketNumber = 0L;
        Optional<TicketStatus> ticketStatus = classUnderTest.ticketStatus(ticketNumber);

        Assert.assertThat(ticketStatus, is(Optional.empty()));
    }

    @Test
    public void testDropTicket() {
        final long ticketNumber = 1L;
        classUnderTest.dropTicket(ticketNumber);

        verifyLock();
        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(tickets, Mockito.times(1)).stream();
        Mockito.verify(tickets, Mockito.times(1)).remove(Matchers.any(Ticket.class));
    }

    @Test
    public void testSize() {
        classUnderTest.size();

        Mockito.verify(redissonClient, Mockito.times(1)).getDeque(TICKETS);
        Mockito.verify(tickets, Mockito.times(1)).size();

    }

    private void verifyLock() {
        Mockito.verify(redissonClient, Mockito.times(1)).getLock(TICKET_LOCK);
        Mockito.verify(rLock, Mockito.times(1)).lock();
        Mockito.verify(rLock, Mockito.times(1)).unlock();
    }

    private List<Ticket> tickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(createTicket(1, 1));
        tickets.add(createTicket(2, 1));
        tickets.add(createTicket(3, 1));
        tickets.add(createTicket(4, 1));
        tickets.add(createTicket(5, 1));
        tickets.add(createTicket(6, 1));
        tickets.add(createTicket(7, 1));
        tickets.add(createTicket(8, 1));
        tickets.add(createTicket(9, 1));
        tickets.add(createTicket(10, 1));
        return tickets;
    }

    private Ticket ticket() {
        return createTicket(1, System.nanoTime());
    }

    private Ticket createTicket(long number, long time) {
        Ticket ticket = new Ticket();
        ticket.setNumber(number);
        ticket.setTime(time);
        return ticket;
    }

    private TicketTime ticketTime() {
        TicketTime ticketTime = new TicketTime();
        ticketTime.setDuration(1);
        ticketTime.setTimeStamp(System.nanoTime());
        return ticketTime;
    }

}