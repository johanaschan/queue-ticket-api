package se.jaitco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Johan Aschan on 2016-08-31.
 */
@Component
public class TicketService {

    private static final String TICKET_KEY = "TICKET_KEY";

    @Autowired
    private JedisPool jedisPool;

    public String takeTicket() {
        String ticket;
        try (Jedis jedis = jedisPool.getResource()) {
            ticket = jedis.lpop(TICKET_KEY);
            if (ticket == null) {
                ticket = "1";
                jedis.lpush(TICKET_KEY, ticket);
            } else {
                Integer newTicket = Integer.parseInt(ticket) + 1;
                ticket = newTicket.toString();
                jedis.lpush(TICKET_KEY, ticket);
            }
        }
        return ticket;
    }

    public void resetTicket() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ltrim(TICKET_KEY, 0, -1);
        }

    }
}
