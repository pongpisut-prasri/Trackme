package manservice.trackmeh.foodtracking.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserModelRepository userModelRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userModelRepository.findByUserName(username);
        if (userModel == null) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        // * list permission
        List<String> subscriptionType = List.of(userModel.getSubscriptionType());
        return UserDetailsImpl.build(userModel, subscriptionType);
    }

}
