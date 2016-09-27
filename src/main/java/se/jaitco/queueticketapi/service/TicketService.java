package se.jaitco.queueticketapi.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.model.TicketTime;
import java.util.Optional;

@Slf4j
@Component
public class TicketService {

    protected static final String TICKETS = "TICKETS";
    protected static final String TICKET_TIMES = "TICKET_TIMES";
    protected static final String TICKET_NUMBER = "TICKET_NUMBER";
    protected static final String TICKET_LOCK = "TICKET_LOCK";

    @Autowired
    private RedissonClient redissonClient;

    public Ticket newTicket() {
        Ticket ticket;
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            RDeque<Ticket> tickets = tickets();
            long newTicketNumber = ticketNumber().incrementAndGet();
            ticket = ticket(newTicketNumber);
            tickets.add(ticket);
        } finally {
            ticketLock.unlock();
        }
        return ticket;
    }

    public void dropTicket(long ticketNumber) {
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            RDeque<Ticket> tickets = tickets();
            tickets.stream()
                    .filter(ticket -> ticket.getNumber() == ticketNumber)
                    .forEach(tickets::remove);
        } finally {
            ticketLock.unlock();
        }
    }

    public void resetTickets() {
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            tickets().delete();
            ticketTimes().delete();
            ticketNumber().set(0);
        } finally {
            ticketLock.unlock();
        }
    }

    public void nextTicket() {
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            Ticket ticket = tickets().poll();
            if (ticket != null) {
                ticketTimes().add(createTicketTimeFromTicket(ticket));
            }
        } finally {
            ticketLock.unlock();
        }
    }

    public Optional<Ticket> currentTicket() {
        RQueue<Ticket> ticketQueue = tickets();
        return Optional.ofNullable(ticketQueue.peek());
    }

    public Optional<TicketStatus> ticketStatus(long ticketNumber) {
        Optional<TicketStatus> ticketStatus = Optional.empty();
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            long numbersBefore = calculateNumbersBefore(tickets(), ticketNumber);
            if (numbersBefore > 0) {
                long estimatedWaitTime = calculateEstimatedWaitTime(numbersBefore);
                ticketStatus = Optional.of(TicketStatus.builder()
                        .numbersBefore(numbersBefore)
                        .estimatedWaitTime(estimatedWaitTime)
                        .build());
            } else {
                ticketStatus = Optional.empty();
            }
        } finally {
            ticketLock.unlock();
        }
        return ticketStatus;
    }

    public Integer size() {
        return tickets().size();
    }

    private long calculateEstimatedWaitTime(long numberBefore) {
        return calculateMeanTime() * numberBefore;
    }

    private long calculateMeanTime() {
        RDeque<TicketTime> ticketTimes = ticketTimes();
        long totalDuration = ticketTimes.stream()
                .mapToLong(TicketTime::getDuration)
                .sum();

        long meanTime = 0;
        if (!ticketTimes.isEmpty()) {
            meanTime = totalDuration / ticketTimes.size();
        }
        return meanTime;
    }

    private long calculateNumbersBefore(RQueue<Ticket> ticketQueue, long ticketNumber) {
        return ticketQueue.stream()
                .filter(ticket -> ticket.getNumber() < ticketNumber)
                .count();
    }

    private Ticket ticket(long number) {
        Ticket ticket = new Ticket();
        ticket.setNumber(number);
        ticket.setTime(System.nanoTime());
        return ticket;
    }

    private TicketTime createTicketTimeFromTicket(Ticket ticket) {
        return createTicketTime(ticket.getTime(), System.nanoTime());
    }

    private TicketTime createTicketTime(long startTime, long endTime) {
        return ticketTime(endTime, endTime - startTime);
    }

    private TicketTime ticketTime(long timeStamp, long duration) {
        TicketTime ticketTime = new TicketTime();
        ticketTime.setTimeStamp(timeStamp);
        ticketTime.setDuration(duration);
        return ticketTime;
    }

    private RDeque<TicketTime> ticketTimes() {
        return redissonClient.getDeque(TICKET_TIMES);
    }

    private RDeque<Ticket> tickets() {
        return redissonClient.getDeque(TICKETS);
    }

    private RLock ticketLock() {
        return redissonClient.getLock(TICKET_LOCK);
    }

    private RAtomicLong ticketNumber() {
        return redissonClient.getAtomicLong(TICKET_NUMBER);
    }

}
