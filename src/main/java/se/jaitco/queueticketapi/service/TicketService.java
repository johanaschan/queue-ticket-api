package se.jaitco.queueticketapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import se.jaitco.queueticketapi.model.Ticket;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Slf4j
@Component
public class TicketService {

    private static final String TICKET_QUEUE = "TICKET_QUEUE";

    private static final String QUEUE_TICKET_NUMBER_KEY = "QUEUE_TICKET_NUMBER_KEY";

    private static final String TICKET_TAKE_KEY = "TICKET_TAKE_KEY";

    private static final String TICKET_NEXT_KEY = "TICKET_NEXT_KEY";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ObjectMapper objectMapper;

    public Ticket takeTicket() {
        RLock takeLock = redissonClient.getLock(TICKET_TAKE_KEY);
        takeLock.lock(1, TimeUnit.SECONDS);
        RAtomicLong atomicLong = redissonClient.getAtomicLong(QUEUE_TICKET_NUMBER_KEY);
        RQueue<Ticket> tickets = redissonClient.getQueue(TICKET_QUEUE);
        Ticket ticket = ticket(atomicLong.getAndIncrement());
        tickets.add(ticket);
        takeLock.unlock();
        return ticket;
    }

    public void resetTickets() {
        redissonClient.getQueue(TICKET_QUEUE).delete();
        redissonClient.getAtomicLong(QUEUE_TICKET_NUMBER_KEY).delete();
    }

    public void nextTicket() {
        RLock nextLock = redissonClient.getLock(TICKET_NEXT_KEY);
        nextLock.lock();
        redissonClient.getQueue(TICKET_QUEUE).poll();
        nextLock.unlock();
    }

    public Optional<Ticket> currentTicket() {
        RQueue<Ticket> ticketQueue = redissonClient.getQueue(TICKET_QUEUE);
        return Optional.ofNullable(ticketQueue.peek());
    }

    private Ticket ticket(long number) {
        Ticket ticket = new Ticket(number,0);
//        ticket.set
 //       Ticket.builder()
 //               .number(number)
//                .time(System.nanoTime())
//                .build();
        return ticket;
    }

}
