package com.example.reserva.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserDetailsService userDetailsService;


    private static final String JWT_REGEX = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";

    @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {


        var token = this.recoverToken(request);

        if (token != null && !request.getServletPath().startsWith("/auth/sign-in")) {
          var userEmail = tokenService.validateToken(token);
          UserDetails existsUser = this.userDetailsService.loadUserByUsername(userEmail);

          request.setAttribute("user", existsUser);

          var newAuthContext = new UsernamePasswordAuthenticationToken(
                  existsUser,
                  null,
                  existsUser.getAuthorities()
          );

          SecurityContextHolder.getContext().setAuthentication(newAuthContext);
      }


      filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null) return null;

        var authToken = authHeader
                .replace("Bearer ", "")
                .replace("undefined", "")
                .replace("null", "");

        return Optional.of(authToken)
                .filter(token -> !token.isEmpty())
                .filter(token -> !token.isBlank())
                .filter(token -> token.matches(JWT_REGEX))
                .orElse(null);
    }

}
