package se.jaitco.queueticketapi.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RDeque;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.jaitco.queueticketapi.model.Ticket;
import se.jaitco.queueticketapi.model.TicketNumber;
import se.jaitco.queueticketapi.model.TicketStatus;
import se.jaitco.queueticketapi.model.TicketTime;

import java.util.Optional;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Slf4j
@Component
public class TicketService {

    private static final String TICKET_QUEUE = "TICKET_QUEUE";
    private static final String TICKET_DURATION = "TICKET_DURATION";
    private static final String TICKET_LOCK = "TICKET_LOCK";

    @Autowired
    private RedissonClient redissonClient;

    public Ticket newTicket() {
        Ticket ticket;
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            RDeque<Ticket> tickets = tickets();
            long newTicketNumber = 1;
            if (!tickets.isEmpty()) {
                newTicketNumber = tickets.peekLast().getNumber() + 1;
            }
            ticket = ticket(newTicketNumber);
            tickets.add(ticket);
        } finally {
            ticketLock.unlock();
        }
        return ticket;
    }

    public void resetTickets() {
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            tickets().delete();
            ticketTimes().delete();
        } finally {
            ticketLock.unlock();
        }
    }

    public void nextTicket() {
        RLock ticketLock = ticketLock();
        ticketLock.lock();
        try {
            Ticket ticket = tickets().poll();
            ticketTimes().add(createTicketTimeFromTicket(ticket));
        } finally {
            ticketLock.unlock();
        }
    }

    public Optional<Ticket> currentTicket() {
        RQueue<Ticket> ticketQueue = tickets();
        return Optional.ofNullable(ticketQueue.peek());
    }

    public TicketStatus getTicketStatus(TicketNumber ticketNumber) {
        Optional<Ticket> currentTicket = currentTicket();
        if (currentTicket.isPresent()) {
            long numberBefore = calculateNumberBefore(currentTicket.get(), ticketNumber);
            long estimatedWaitTime = calculateEstimatedWaitTime(numberBefore);
            return new TicketStatus(numberBefore, estimatedWaitTime);
        } else {
            return new TicketStatus(0, 0);
        }
    }

    private long calculateEstimatedWaitTime(long numberBefore) {
        return calculateMeanTime() * numberBefore;
    }

    private long calculateMeanTime() {
        long count = 0;
        long totalDuration = 0;
        for (TicketTime ticketTime : ticketTimes()) {
            totalDuration = ticketTime.getDuration();
            count++;
        }
        if (count == 0) {
            return 1000;
        }
        return totalDuration / count;
    }

    private long calculateNumberBefore(Ticket currentTicket, TicketNumber ticketNumber) {
        return ticketNumber.getNumber() - currentTicket.getNumber();
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
        return redissonClient.getDeque(TICKET_DURATION);
    }

    private RDeque<Ticket> tickets() {
        return redissonClient.getDeque(TICKET_QUEUE);
    }

    private RLock ticketLock() {
        return redissonClient.getLock(TICKET_LOCK);
    }

}
