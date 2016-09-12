package se.jaitco.queueticketapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@Data
//@EqualsAndHashCode(exclude={"time"})
public class Ticket {

    private long time;

    private long number;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return number == ticket.number;

    }

    @Override
    public int hashCode() {
        return (int) (number ^ (number >>> 32));
    }
}
