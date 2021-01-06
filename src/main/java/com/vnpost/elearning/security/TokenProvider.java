package com.vnpost.elearning.security;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * May 14,2020
 */
@Component
public class TokenProvider {
    private static final String SIGNING_KEY="eln_vnpost_by_java_team";
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000*60*60*24;
    private static final String UNITCODE = "unit";
    private static final String AUTHORITIES_KEY = "scopes";
    private static final String FULLNAME = "FULL_NAME";
    private static final String ID = "id";

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        final MyUser myUser = (MyUser) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(UNITCODE,myUser.getPosCode())
                .claim(ID,myUser.getId())
                .claim(FULLNAME,myUser.getFullName())
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

    UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final MyUser userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

//        final Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
        final Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        final String poscode = (String) claims.get(UNITCODE);
        final String fullName = (String) claims.get(FULLNAME);
        userDetails.setFullName(fullName);
        userDetails.setPosCode(poscode);
        final Long id = Long.valueOf( (Integer) claims.get(ID)) ;
        userDetails.setId(id);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
