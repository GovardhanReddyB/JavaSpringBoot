package org.govardhan.userregistrationservice.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationDetailsService extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
}
