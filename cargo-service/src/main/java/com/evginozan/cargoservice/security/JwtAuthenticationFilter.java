package com.evginozan.cargoservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // API Gateway tarafından eklenen header'ları oku
        String userId = request.getHeader("X-Auth-UserId");
        String roles = request.getHeader("X-Auth-Roles");

        logger.debug("User ID from header: " + userId);
        logger.debug("Roles from header: " + roles);

        if (StringUtils.hasText(userId) && StringUtils.hasText(roles)) {
            List<SimpleGrantedAuthority> authorities = parseRoles(roles);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> parseRoles(String rolesString) {
        if (rolesString == null || rolesString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(roleArray)
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}