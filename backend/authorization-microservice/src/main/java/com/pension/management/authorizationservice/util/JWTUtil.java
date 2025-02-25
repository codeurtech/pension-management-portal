package com.pension.management.authorizationservice.util;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pension.management.authorizationservice.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JWTUtil {

	@Value("${jwt.signing.key.secret}")
	private String secret;

	public static final long JWT_TOKEN_VALIDITY = 60L * 30L;

	public String generateToken(String subject) {
		return Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis())).setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(secret.getBytes())).compact();
	}

	public String getUsernameFromToken(String token) {
		return getClaims(token).getSubject();
	}

	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(Base64.getEncoder().encode(secret.getBytes())).parseClaimsJws(token)
				.getBody();
	}

	public boolean isTokenExpiredOrInvalidFormat(String token) throws InvalidTokenException {
		try {
			getClaims(token);
		} catch (ExpiredJwtException e) {
			throw new InvalidTokenException("TOKEN EXPIRED");
		} catch (MalformedJwtException e) {
			throw new InvalidTokenException("INVALID TOKEN FORMAT");
		} catch (IllegalArgumentException e) {
			throw new InvalidTokenException("NULL/EMPTY TOKEN");
		} catch (SignatureException e) {
			throw new InvalidTokenException("INVALID TOKEN SIGNATURE");
		}
		return false;
	}

}