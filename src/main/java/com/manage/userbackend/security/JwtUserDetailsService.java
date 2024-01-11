package com.manage.userbackend.security;

import com.manage.userbackend.exceptions.AuthorizationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private static Logger logger = LogManager.getLogger(JwtUserDetailsService.class);

    private final JwtTokenUtil tokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(Arrays.stream(JwtTypes.values()).anyMatch(jwtTypes -> username.equals(jwtTypes.name()))) {
            return new User(username, "EDv+UY+Yp1Ccp533SPtFew==",
                    new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public Claims authenticate(HttpServletRequest request, JwtTypes... jwtTypes) throws AuthorizationException {
        logger.info(request.getRequestURI() + " | Access Rights | " + Arrays.toString(jwtTypes));
        String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = null;

        if(requestTokenHeader != null &&
                requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring("Bearer ".length());
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if(tokenUtil.validateToken(jwtToken, jwtTypes)){
            return tokenUtil.getAllClaimsFromToken(jwtToken);
        }

        throw new AuthorizationException("Forbidden");
    }
}
