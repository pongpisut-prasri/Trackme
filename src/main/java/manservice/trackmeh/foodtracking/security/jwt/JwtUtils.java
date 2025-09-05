package manservice.trackmeh.foodtracking.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;

@Component
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtUtils {

  private final JwtProps jwtProps;

  private SecretKey key;

  @PostConstruct
  public void init() {
    key = new SecretKeySpec(jwtProps.getJwtSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }

  public String generateJwtToken(Authentication authentication) throws JsonProcessingException {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtProps.getJwtExpirationMs());

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    JwtModel jwtModel = new JwtModel();
    BeanUtils.copyProperties(userPrincipal.getUserModel(), jwtModel);
    Map<String, Object> claims = new HashMap<>();
    String serializedUserDetails = objectMapper.writeValueAsString(jwtModel);
    claims.put("USER_LOGIN", serializedUserDetails);

    // Build and sign the JWT token
    String token = Jwts.builder()
        .setSubject(userPrincipal.getUsername())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .addClaims(claims) // Add your custom claims here
        .signWith(key)
        .compact();

    return token;
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  // Extract the user detail from the JWT token
  public UserModel getUserDetail(String token) throws JsonMappingException, JsonProcessingException {
    token = AuthTokenFilter.parseJwt(token);
    // Parse and verify the JWT token
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    // Retrieve your custom claim
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
    String serializedUserDetails = (String) claims.get("USER_LOGIN");

    UserModel userRegister = objectMapper.readValue(serializedUserDetails,
        UserModel.class);

    return userRegister;
  }

  @SneakyThrows
  public JwtModel getUserTokenDetailFromTokenForAdminAccess(String token) {
    token = AuthTokenFilter.parseJwt(token);
    // Parse and verify the JWT token
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    // Retrieve your custom claim
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
    String serializedUserDetails = (String) claims.get("USER_LOGIN");

    return objectMapper.readValue(serializedUserDetails, JwtModel.class);
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    }
    return false;
  }

}
