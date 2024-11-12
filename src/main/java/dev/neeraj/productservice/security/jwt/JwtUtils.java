package dev.neeraj.productservice.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.neeraj.productservice.security.models.Role;
import dev.neeraj.productservice.security.models.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpireInHrs}")
    private String jwtExpireInHrs;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookieName;

    @Autowired
    private ObjectMapper objectMapper;

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        System.out.println("Cookies:  " + Arrays.toString(cookies));
        if (cookies == null) return "";

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(jwtCookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }

    public boolean isValidJwt(String jwt){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(jwt);

            return true;
        } catch (ExpiredJwtException ignored){
            System.out.println("JWT is expired");
        } catch (MalformedJwtException ignored){
            System.out.println("JWT is malformed");
        } catch (SignatureException ignored){
            System.out.println("JWT key is invalid");
        } catch (IllegalArgumentException ignored){
            System.out.println("JWT is invalid - JWT: " + jwt);
        }

        return false;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    public String getSubjectFromJwt(String jwt){

        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public UserDetailsImpl getUserDetailsFromJwt(String jwt) throws JsonProcessingException {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setEmail(body.get("email", String.class));
        userDetails.setFirstname(body.get("firstname", String.class));
        userDetails.setLastname(body.get("lastname", String.class));
        userDetails.setId(body.get("id", Long.class));
        userDetails.setRoles(
                Arrays.stream(body.get("roles", String.class).split(","))
                        .map(Role::new)
                        .toList()
        );

        return userDetails;
    }
}
