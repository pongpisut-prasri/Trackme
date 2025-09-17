package manservice.trackmeh.foodtracking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import manservice.trackmeh.foodtracking.entity.SubscriptionPermission;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.SubscriptionPermissionRepository;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private SubscriptionPermissionRepository subscriptionPermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userModelRepository.findByUserName(username);
        if (userModel == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        // * list permission
        SubscriptionPermission permission = subscriptionPermissionRepository.findBySubscriptionType(userModel.getSubscriptionType());
        List<SimpleGrantedAuthority> authorities = permission.getPermission().stream().map(o->new SimpleGrantedAuthority(o)).collect(Collectors.toList());
        List<String> subscriptionType = List.of(userModel.getSubscriptionType());
        return UserDetailsImpl.build(userModel, subscriptionType);
    }

}
