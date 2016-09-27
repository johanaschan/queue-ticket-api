package se.jaitco.queueticketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import se.jaitco.queueticketapi.filter.JwtFilter;

@SpringBootApplication
public class QueueTicketApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueTicketApiApplication.class, args);
    }
}
