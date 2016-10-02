package se.jaitco.queueticketapi.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_WITH_SPACE = "Bearer ";
    private static final String OPTIONS = "OPTIONS";
    private static final int BEARER_WITH_SPACE_LENGTH = BEARER_WITH_SPACE.length();


    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if (isPreflight(request)) {
            chain.doFilter(req, res);
        } else {
            final String authHeader = getAuthHeader(request);
            if (isHeaderValid(authHeader)) {
                final String token = getToken(authHeader);
                try {
                    final Claims claims = verifySignatureAndGetClaims(token);
                    request.setAttribute("claims", claims);
                    chain.doFilter(req, res);
                } catch (SignatureException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            }
        }
    }

    private Claims verifySignatureAndGetClaims(String token) {
        return Jwts.parser()
                .setSigningKey("secretkey")
                .parseClaimsJws(token)
                .getBody();
    }

    private String getToken(String authHeader) {
        return authHeader.substring(BEARER_WITH_SPACE_LENGTH);
    }

    private boolean isHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_WITH_SPACE);
    }

    private String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    private boolean isPreflight(HttpServletRequest request) {
        return "OPTIONS".equals(request.getMethod());
    }
}