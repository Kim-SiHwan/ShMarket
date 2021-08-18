package kim.sihwan.daangn.config.jwt;

import io.jsonwebtoken.*;
import kim.sihwan.daangn.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final MemberService memberService;
    public JwtTokenProvider (@Lazy MemberService memberService){
        this.memberService = memberService;
    }

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token-validity-in-seconds}")
    long tokenValidityInSeconds;

    public String createToken(String username){

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("auth", "ROLE_USER")
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .setIssuedAt(new Date(now))
                .compact();


    }
    public Authentication getAuthentication(String token){
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        UserDetails userDetails = memberService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("SecurityException | MalformedJwtException e");
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException e");
        } catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException e");
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException e");
        }
        return false;
    }

}
