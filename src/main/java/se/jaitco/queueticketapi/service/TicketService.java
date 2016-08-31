package se.jaitco.queueticketapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import se.jaitco.queueticketapi.model.Ticket;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Slf4j
@Component
public class TicketService {

    private static final String TICKET_KEY = "TICKET_KEY";

    private static final String QUEUE_TICKET_NUMBER_KEY = "QUEUE_TICKET_NUMBER_KEY";

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ObjectMapper objectMapper;

    public synchronized Ticket takeTicket() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(QUEUE_TICKET_NUMBER_KEY);
        RQueue<Ticket> tickets = redissonClient.getQueue(TICKET_KEY);
        Ticket ticket;
        try (Jedis jedis = jedisPool.getResource()) {
            String ticketString = jedis.lindex(TICKET_KEY, -1);
            if (ticketString == null) {
                ticket = ticket(1);
                jedis.rpush(TICKET_KEY, writeObjectAsString(ticket));
            } else {
                ticket = readValue(ticketString);
                int newNumber = ticket.getNumber() + 1;
                ticket = ticket(newNumber);
                jedis.rpush(TICKET_KEY, writeObjectAsString(ticket));
            }
        }
        return ticket;
    }

    public synchronized void resetTickets() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(TICKET_KEY);
        }
    }

    public synchronized void nextTicket() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ltrim(TICKET_KEY, 1, -1);
        }
    }

    public synchronized Optional<Ticket> currentTicket() {
        try (Jedis jedis = jedisPool.getResource()) {
            String ticketString = jedis.lindex(TICKET_KEY, 0);
            if (ticketString == null) {
                return Optional.empty();
            } else {
                return Optional.of(readValue(ticketString));
            }
        }
    }

    private Ticket ticket(int number) {
        return Ticket.builder()
                .number(number)
                .time(System.nanoTime())
                .build();
    }

    private Ticket readValue(String ticketString) {
        Ticket ticket;
        try {
            ticket = objectMapper.readValue(ticketString, Ticket.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ticket;
    }

    private String writeObjectAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
