package manservice.trackmeh.foodtracking.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import manservice.trackmeh.foodtracking.entity.UserModel;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private UserModel userModel;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public static UserDetailsImpl build(UserModel user, List<String> userSubscription) {
        List<GrantedAuthority> authorities = userSubscription.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUsername(),
                user.getPasswordEncrypted(),
                true,
                true,
                true,
                true,
                user,
                authorities);
    }

}
