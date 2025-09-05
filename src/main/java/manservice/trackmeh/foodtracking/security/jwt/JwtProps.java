package manservice.trackmeh.foodtracking.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "trackme.jwt-config")
public class JwtProps {

    private String jwtSecret;

    private int jwtExpirationMs;

}
