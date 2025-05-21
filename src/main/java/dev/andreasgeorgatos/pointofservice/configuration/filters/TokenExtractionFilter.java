package dev.andreasgeorgatos.pointofservice.configuration.filters;

import dev.andreasgeorgatos.pointofservice.configuration.JWTUtil;
import dev.andreasgeorgatos.pointofservice.configuration.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwe;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter responsible for extracting and validating JWT tokens from incoming requests.
 * If a valid token is found, it sets the authentication context for Spring Security.
 * This filter extends {@link OncePerRequestFilter} to ensure it's executed once per request.
 */
@Component
public class TokenExtractionFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenExtractionFilter.class);
    private final JWTUtil jwtUtil;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    private static final String AUTH_CLAIM = "auth";

    private static final String LOGIN_PATH = "/api/v1/users/login";
    private static final String REGISTER_PATH = "/api/v1/users/register";
    private static final String FORGOT_PASSWORD_PATH = "/api/v1/users/forgotPassword";
    private static final String RESET_PASSWORD_PATH = "/api/v1/users/resetPassword";


    /**
     * Constructs a new TokenExtractionFilter.
     *
     * @param jwtUtil The utility class for JWT operations.
     */
    public TokenExtractionFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Processes an incoming HTTP request, extracts the JWT token, validates it,
     * and sets the authentication in the security context.
     *
     * @param request     The {@link HttpServletRequest} object that contains the request the client made of the servlet.
     * @param response    The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @param filterChain The {@link FilterChain} for invoking the next filter or the resource.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Skip token extraction for public endpoints
        if (LOGIN_PATH.equals(path) || REGISTER_PATH.equals(path) || FORGOT_PASSWORD_PATH.equals(path) || RESET_PASSWORD_PATH.equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        // If Authorization header is missing or not Bearer, proceed without authentication
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX_LENGTH);

        try {
            Jwe<Claims> claimsJwe = jwtUtil.getClaims(token);
            Claims payload = claimsJwe.getPayload();
            String username = payload.getSubject();

            @SuppressWarnings("unchecked") // Suppressing warning for casting Object to List<String>
            List<String> roles = payload.get(AUTH_CLAIM, List.class);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            if (roles != null) {
                for (String role : roles) {
                    authorityList.add(new SimpleGrantedAuthority(role));
                }
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired: {} for path: {}", e.getMessage(), path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
            return;
        } catch (MalformedJwtException e) {
            logger.warn("JWT token is malformed: {} for path: {}", e.getMessage(), path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is malformed");
            return;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {} for path: {}", e.getMessage(), path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token signature validation failed");
            return;
        } catch (Exception e) {
            logger.error("Error processing JWT token: {} for path: {}", e.getMessage(), path, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}


