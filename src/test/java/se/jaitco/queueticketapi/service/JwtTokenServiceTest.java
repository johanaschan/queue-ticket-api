package se.jaitco.queueticketapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.jaitco.queueticketapi.model.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JwtTokenServiceTest {

    @InjectMocks
    private final JwtTokenService classUnderTest = new JwtTokenServiceTestClass();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private static String USERNAME = "USERNAME";

    private static String PASSWORD = "PASSWORD";

    private static String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6IlVTRVJOQU1FIiwiZXhwIjoyNTMwNTY2MDAwfQ.QZvriUdoD-wNz8bfPgR0nePx2V_H71fSYXfx7GObc0ew71t9ZRiC-PSM9SzixuPn8HG-DrVPg6IxZ5P-nQnLcA";

    private UserDetailsImpl userDetails = UserDetailsImpl.builder().username(USERNAME).password(PASSWORD).build();


//    @Test
    public void testBuildJWTUser() throws ServletException, IOException {
        User user = User.builder().username(USERNAME).password(PASSWORD).grantedRoles(Arrays.asList(Roles.CUSTOMER)).build();
        UserDetailsImpl userDetails = classUnderTest.buildJWTUser(user);
        Assert.assertEquals(USERNAME,userDetails.getUsername());
        Assert.assertEquals(PASSWORD,userDetails.getPassword());
        Assert.assertTrue(userDetails.getAuthorities().contains( new SimpleGrantedAuthority(Roles.CUSTOMER.name())));
    }

  //  @Test
    public void testGenerateToken() throws ServletException, IOException {
        String token = classUnderTest.generateToken(userDetails);
        System.out.println(token);
        Assert.assertEquals(TOKEN,token);
    }

  //  @Test
    public void testValidateToken() throws ServletException, IOException {
        Assert.assertTrue(classUnderTest.validateToken(TOKEN,userDetails));
    }

  //  @Test
    public void testGetUsernameFromToken(){
        String username = classUnderTest.getUsernameFromToken(TOKEN);
        Assert.assertEquals(USERNAME,username);
    }

  //  @Test
    public void getExpirationDateFromToken(){
       Date date = classUnderTest.getExpirationDateFromToken(TOKEN);
        Date date2 = classUnderTest.generateExpirationDate();
        Assert.assertEquals(date,date2);
    }

}