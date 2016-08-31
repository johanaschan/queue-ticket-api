package se.jaitco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import se.jaitco.model.Ticket;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Slf4j
@Component
public class TicketService {

    private static final String TICKET_KEY = "TICKET_KEY";

    private final Object lock = new Object();

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ObjectMapper objectMapper;

    public Ticket takeTicket() {
        log.info("takeTicket");
        Ticket ticket;
        try (Jedis jedis = jedisPool.getResource()) {
            synchronized (lock) {
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
            log.info(jedis.llen(TICKET_KEY).toString());
        }
        return ticket;
    }

    public void resetTickets() {
        log.info("resetTickets");
        try (Jedis jedis = jedisPool.getResource()) {
            synchronized (lock) {
                jedis.del(TICKET_KEY);
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
