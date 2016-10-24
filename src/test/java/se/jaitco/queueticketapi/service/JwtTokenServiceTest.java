package se.jaitco.queueticketapi.service;

import org.junit.Assert;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.jaitco.queueticketapi.model.Roles;
import se.jaitco.queueticketapi.model.User;
import se.jaitco.queueticketapi.model.UserDetailsImpl;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JwtTokenServiceTest {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6IlVTRVJOQU1FIiwiZXhwIjoyNTMwNTY2MDAwfQ.QZvriUdoD-wNz8bfPgR0nePx2V_H71fSYXfx7GObc0ew71t9ZRiC-PSM9SzixuPn8HG-DrVPg6IxZ5P-nQnLcA";

    private final JwtTokenService classUnderTest = new JwtTokenServiceTestClass();

    private UserDetailsImpl userDetails = UserDetailsImpl.builder().username(USERNAME).password(PASSWORD).build();

    //    @Test
    public void testBuildJWTUser() throws ServletException, IOException {
        User user = User.builder().username(USERNAME).password(PASSWORD).grantedRoles(Arrays.asList(Roles.CUSTOMER)).build();
        UserDetailsImpl userDetails = classUnderTest.buildJWTUser(user);
        Assert.assertEquals(USERNAME, userDetails.getUsername());
        Assert.assertEquals(PASSWORD, userDetails.getPassword());
        Assert.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(Roles.CUSTOMER.name())));
    }

    //  @Test
    public void testGenerateToken() throws ServletException, IOException {
        String token = classUnderTest.generateToken(userDetails);
        System.out.println(token);
        Assert.assertEquals(TOKEN, token);
    }

    //  @Test
    public void testValidateToken() throws ServletException, IOException {
        Assert.assertTrue(classUnderTest.validateToken(TOKEN, userDetails));
    }

    //  @Test
    public void testGetUsernameFromToken() {
        String username = classUnderTest.getUsernameFromToken(TOKEN);
        Assert.assertEquals(USERNAME, username);
    }

    //  @Test
    public void getExpirationDateFromToken() {
        Date date = classUnderTest.getExpirationDateFromToken(TOKEN);
        Date date2 = classUnderTest.generateExpirationDate();
        Assert.assertEquals(date, date2);
    }

    private static class JwtTokenServiceTestClass extends JwtTokenService {

        @Override
        protected Date generateExpirationDate() {
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


}