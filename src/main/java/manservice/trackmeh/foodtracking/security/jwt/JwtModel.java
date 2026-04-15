package manservice.trackmeh.foodtracking.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtModel {
    private Object token;
    private String userId;
    private String username;
    // private String password;
    private String subscriptionType;
    // private String jwt;

    // public JwtModel(Object token,
    //         String userId,
    //         String username,
    //         String subscriptionType) {
    //     this.token = token;
    //     this.userId = userId;
    //     this.username = username;
    //     this.subscriptionType = subscriptionType;
    // }
}
