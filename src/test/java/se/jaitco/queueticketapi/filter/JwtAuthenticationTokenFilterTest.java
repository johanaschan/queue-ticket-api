package se.jaitco.queueticketapi.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import se.jaitco.queueticketapi.model.UserDetailsImpl;
import se.jaitco.queueticketapi.service.JwtTokenService;
import se.jaitco.queueticketapi.service.SecurityContextHolderService;
import se.jaitco.queueticketapi.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtAuthenticationTokenFilterTest {

    @InjectMocks
    private final JwtAuthenticationTokenFilter classUnderTest = new JwtAuthenticationTokenFilter();

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private SecurityContextHolderService securityContextHolderService;

    @Mock
    private UserDetailsServiceImpl userServiceDetail;

    private String token = "Bearer: token";

    private String username = "username";

    private String password = "password";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
        UserDetails userDetail = UserDetailsImpl.builder().username(username).password(password).build();
        when(request.getHeader(JwtAuthenticationTokenFilter.AUTHORIZATION)).thenReturn(token);
        when(jwtTokenService.getUsernameFromToken(token)).thenReturn(username);
        when(securityContextHolderService.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(null,null,null));
        when(userServiceDetail.loadUserByUsername(username)).thenReturn(userDetail);
        classUnderTest.doFilter(request,response,chain);
        verify(securityContextHolderService,(times(1))).getAuthentication();
//        verify(securityContextHolderService,(times(1))).setAuthentication(Matchers.any(UsernamePasswordAuthenticationToken.class));

    }

}
