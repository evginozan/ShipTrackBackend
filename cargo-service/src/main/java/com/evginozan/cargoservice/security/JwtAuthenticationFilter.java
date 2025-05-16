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

        // Debug için header'ları loglayalım
        logger.debug("Headers received by Cargo Service:");
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                logger.debug(headerName + ": " + request.getHeader(headerName)));

        // API Gateway tarafından eklenen header'ları oku
        String userId = request.getHeader("X-Auth-UserId");
        String roles = request.getHeader("X-Auth-Roles");

        logger.debug("User ID from header: " + userId);
        logger.debug("Roles from header: " + roles);

        // Eğer kimlik bilgileri doğrulanmışsa
        if (StringUtils.hasText(userId) && StringUtils.hasText(roles)) {
            // Rolleri parse et
            List<SimpleGrantedAuthority> authorities = parseRoles(roles);

            // Debug için rolleri loglayalım
            logger.debug("Parsed authorities: " + authorities);

            // Güvenlik bağlamını oluştur
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);

            // Güvenlik bağlamını ayarla
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Authentication set in SecurityContextHolder");
        } else {
            logger.debug("No valid headers found for JWT authentication");
        }

        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> parseRoles(String rolesString) {
        // Bu kısımda önemli bir düzeltme yapıyoruz - roles stringini doğru ayrıştırmak için
        // Gelen veri "ROLE_ADMIN" gibi tek bir String olabilir

        if (rolesString == null || rolesString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Köşeli parantezlerden temizle (varsa)
        String cleanRoles = rolesString.replace("[", "").replace("]", "");

        // Virgüllerle ayrılmış ise parçala, değilse tek rol olarak kabul et
        String[] roleArray = cleanRoles.contains(",")
                ? cleanRoles.split(",")
                : new String[] { cleanRoles };

        return Arrays.stream(roleArray)
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}