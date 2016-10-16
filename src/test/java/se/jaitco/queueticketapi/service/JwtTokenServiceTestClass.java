package se.jaitco.queueticketapi.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JwtTokenServiceTestClass extends JwtTokenService{

    @Override
    protected Date generateExpirationDate(){
        Calendar myCalendar = new GregorianCalendar(2050, 2, 11);
        Date date = myCalendar.getTime();
        return date;
    }

    @Override
    protected Date getTodaysDate() {
        Calendar myCalendar = new GregorianCalendar(2050, 2, 10);
        Date date = myCalendar.getTime();
        return date;
    }
}
