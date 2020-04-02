package com.thomaster.ourcloud.auth;

import com.thomaster.ourcloud.model.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        if (authentication != null)
            SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);

        if (token != null && !token.equals("") && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {

            byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

            String username = parsedToken
                    .getBody()
                    .getSubject();

            String role = (String) parsedToken.getBody().get("rol");
            verifyRole(role);

            return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));
        }

        return null;
    }

    private String verifyRole(String role) {

        try {
            Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Non existent role in request");
        }

        return role;
    }
}
