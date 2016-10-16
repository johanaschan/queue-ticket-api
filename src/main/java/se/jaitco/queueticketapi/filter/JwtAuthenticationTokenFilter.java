package se.jaitco.queueticketapi.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import se.jaitco.queueticketapi.service.JwtTokenService;
import se.jaitco.queueticketapi.service.SecurityContextHolderService;
import se.jaitco.queueticketapi.service.UserDetailsServiceImpl;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    protected static final String AUTHORIZATION = "Authorization";
    protected static final String BEARER_WITH_SPACE = "Bearer ";
    protected static final int BEARER_WITH_SPACE_LENGTH = BEARER_WITH_SPACE.length();

    @Autowired
    SecurityContextHolderService securityContextHolderService;

    @Autowired
    UserDetailsServiceImpl userServiceDetail;

    @Autowired
    JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);
        authToken = getAuthToken(authToken);
        String username = getUsernameFromToken(authToken);

        doAuthentication(request, authToken, username);

        chain.doFilter(request, response);
    }

    private void doAuthentication(HttpServletRequest request, String authToken, String username) {
        if (username != null && securityContextHolderService.getAuthentication() == null) {
            UserDetails userDetails = this.userServiceDetail.loadUserByUsername(username);
            if (jwtTokenService.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContextHolderService.setAuthentication(authentication);
            }
        }
    }

    private String getUsernameFromToken(String authToken) {
        String username = null;
        try {
            username = jwtTokenService.getUsernameFromToken(authToken);
        }catch(Exception e){

        }
        return username;
    }

    private String getAuthToken(String authToken) {
        if(authToken != null && authToken.startsWith(BEARER_WITH_SPACE)) {
            authToken = authToken.substring(BEARER_WITH_SPACE_LENGTH);
        }
        return authToken;
    }

}

