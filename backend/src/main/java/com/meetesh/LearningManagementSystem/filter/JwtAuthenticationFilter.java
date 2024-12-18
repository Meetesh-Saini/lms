package com.meetesh.LearningManagementSystem.filter;

import com.meetesh.LearningManagementSystem.service.UserDetailsService;
import com.meetesh.LearningManagementSystem.utilities.JwtAuthenticationToken;
import com.meetesh.LearningManagementSystem.utilities.JwtTokenUtil;
import com.meetesh.LearningManagementSystem.entry.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MissingClaimException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            String token = jwtTokenUtil.extractTokenFromHeader(request.getHeader("Authorization"));

            if (token != null && jwtTokenUtil.validateToken(token, (User) userDetailsService.loadUserByUsername(jwtTokenUtil.extractUsername(token)))) {
                var userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.extractUsername(token));
                var authentication = new JwtAuthenticationToken(userDetails, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException ex) {
            ApiResponse<String> responsePayload = new ApiResponse<>("jwt-error", "Token expired", false, HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responsePayload));
            return;
            // throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "Token expired");
        } catch (MissingClaimException ex) {
            ApiResponse<String> responsePayload = new ApiResponse<>("jwt-error", "Token missing", false, HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responsePayload));
            return;
            // throw new MissingClaimException(ex.getHeader(), ex.getClaims(), "Token missing");
        } catch (JwtException ex) {
            ApiResponse<String> responsePayload = new ApiResponse<>("jwt-error", "Invalid Token", false, HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(responsePayload));
            return;
            // throw new JwtException("Invalid Token");
        }

        chain.doFilter(request, response);
    }
}
