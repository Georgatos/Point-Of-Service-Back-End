package dev.andreasgeorgatos.tsilikos.configuration.filters;

import dev.andreasgeorgatos.tsilikos.configuration.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwe;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TokenExtractionFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    public TokenExtractionFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.equals("/api/v1/users/login") || path.equals("/api/v1/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        Jwe<Claims> claims = jwtUtil.getClaims(token);

        String username = claims.getPayload().getSubject();

        List<GrantedAuthority> authorityList = new ArrayList<>();

        for (Object authority : claims.getPayload().get("auth", List.class)) {
            authorityList.add(new SimpleGrantedAuthority(authority.toString()));
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorityList);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}


