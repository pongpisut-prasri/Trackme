package manservice.trackmeh.foodtracking.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.imageio.IIOException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;
import manservice.trackmeh.foodtracking.security.jwt.JwtModel;
import manservice.trackmeh.foodtracking.security.jwt.JwtUtils;
import manservice.trackmeh.foodtracking.service.UserService;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserModelRepository userModelRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public UserModel getUserModelByUsername(String username) {
        return userModelRepository.findByUsername(username);
    }

    @Override
    @SneakyThrows
    public UserModel createUser(UserReq req) {
        // Validate if username exists in db, throw exception if it exists
        if (StringUtils.isEmpty(req.getUsername()) && StringUtils.isEmpty(req.getPassword())) {
            throw new IOException("username/password is required");
        }
        if (StringUtils.isEmpty(req.getUsername())) {
            throw new IOException("username is required");
        }
        if (StringUtils.isEmpty(req.getPassword())) {
            throw new IOException("password is required");
        }
        if (userModelRepository.findByUsername(req.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserModel user = new UserModel();
        user.setUsername(req.getUsername());
        user.setPasswordEncrypted(passwordEncoder.encode(req.getPassword())); // Swap with encoder.encode(rawPassword)
                                                                              // if encoder is accessible
        user.setCreateDate(LocalDateTime.now());
        user.setSubscriptionType("FREE");
        return userModelRepository.save(user);
    }

    @Override
    @SneakyThrows
    public void updateUserJwt(String userId, String jwt) {
        Optional<UserModel> userModel = userModelRepository.findById(userId);
        if (!userModel.isPresent()) {
            throw new Exception("user not found");

        }
        UserModel userModelGet = userModel.get();
        userModelGet.setJwt(jwt);
        userModelGet.setUpdateDate(LocalDateTime.now());
        userModelRepository.save(userModelGet);
    }

    @Override
    public JwtModel login(UserReq req) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(),
                        req.getPassword()));
        // ทำให้ spring secure รู้ว่า user นี้ authenticated แล้ว
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            Object jwt = jwtUtil.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return new JwtModel(jwt,
                    userDetails.getUserModel().getId(),
                    userDetails.getUserModel().getUsername(),
                    userDetails.getUserModel().getSubscriptionType());
        } catch (Exception e) {
            log.error(e, e);
            throw new Exception(e.getMessage());
        }
        // authentication = UserDetailServiceImpl.loadByUsername after compare result
        // line 103

    }

}
