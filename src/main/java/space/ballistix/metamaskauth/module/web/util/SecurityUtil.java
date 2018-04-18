package space.ballistix.metamaskauth.module.web.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public class SecurityUtil {
    public static boolean isSignedIn(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && !"anonymousUser".equalsIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public static String generateJWT(String subject, String secret){
        return Jwts.builder()
            .setSubject(subject)
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours from now
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public static String getPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null ?
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() : "anonymousUser";
    }
}
