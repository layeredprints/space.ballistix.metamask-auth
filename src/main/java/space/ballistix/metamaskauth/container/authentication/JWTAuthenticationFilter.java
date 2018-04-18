package space.ballistix.metamaskauth.container.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import space.ballistix.metamaskauth.container.configuration.JWTConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private final JWTConfig jwtConfig;

    public JWTAuthenticationFilter(AuthenticationManager authManager, JWTConfig jwtConfig) {
        super(authManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        Cookie jwtTokenCookie = null;
        if(req.getCookies() != null){
            jwtTokenCookie = Arrays.stream(req.getCookies())
                    .filter(c -> "Authorization".equalsIgnoreCase(c.getName()))
                    .findAny()
                    .orElse(null);
        }

        if (jwtTokenCookie == null) {
            chain.doFilter(req, res);
            return;
        }

        String token = jwtTokenCookie.getValue();
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null) {
            try {
                // parse the token.
                Jws<Claims> claims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecret())
                        .parseClaimsJws(token);

                // Get subject
                String subject = claims.getBody().getSubject();
                List<GrantedAuthority> roles = new ArrayList<>();
                return new UsernamePasswordAuthenticationToken(subject, null, roles);
            } catch (Exception e){
                // ignore
            }
        }
        return null;
    }
}