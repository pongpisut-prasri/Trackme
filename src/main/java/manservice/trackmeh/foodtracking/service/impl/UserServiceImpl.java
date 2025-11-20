package manservice.trackmeh.foodtracking.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.IIOException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;
import manservice.trackmeh.foodtracking.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserModel getUserModelByUsername(String username) {
        return userModelRepository.findByUserName(username);
    }
    @Override
    @SneakyThrows
    public UserModel createUser(UserReq req) {
        // Validate if username exists in db, throw exception if it exists
        if (StringUtils.isEmpty(req.getUsername())&&StringUtils.isEmpty(req.getPassword())) {
            throw new IOException("username/password is required");
        }
        if (StringUtils.isEmpty(req.getUsername())) {
            throw new IOException("username is required");
        }
        if (StringUtils.isEmpty(req.getPassword())) {
            throw new IOException("password is required");
        }
        if (userModelRepository.findByUserName(req.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserModel user = new UserModel();
        user.setUsername(req.getUsername());
        user.setPasswordEncrypted(passwordEncoder.encode(req.getPassword())); // Swap with encoder.encode(rawPassword) if encoder is accessible
        user.setCreateDate(LocalDateTime.now());
        user.setSubscriptionType("FREE");
        return userModelRepository.save(user);
    }
    
}
