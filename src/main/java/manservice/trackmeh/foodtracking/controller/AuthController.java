package manservice.trackmeh.foodtracking.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.entity.UserNutritionPlan;
// import manservice.trackmeh.foodtracking.repository.UserModelRepository;
// import manservice.trackmeh.foodtracking.security.JwtUtil;
import manservice.trackmeh.foodtracking.repository.UserModelRepository;
// import manservice.trackmeh.foodtracking.security.JwtUtil;
import manservice.trackmeh.foodtracking.repository.UserNutritionPlanRepository;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;
import manservice.trackmeh.foodtracking.security.jwt.JwtModel;
import manservice.trackmeh.foodtracking.security.jwt.JwtUtils;

@Log4j2
@RestController
@RequestMapping("/demo/auth")
public class AuthController {

    @Autowired
    private UserModelRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    // @Autowired
    // private UserNutritionPlanRepository userNutritionPlanRepository;

    @Autowired
    private JwtUtils jwtUtil;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public BaseResponse register(@Valid @RequestBody UserReq req) {
        try {
            String passwordEncrypted = passwordEncoder.encode(req.getPassword());
            UserModel userEntity = new UserModel();
            userEntity.setUsername(req.getUsername());
            userEntity.setPasswordEncrypted(passwordEncrypted);
            userEntity.setCreateDate(LocalDateTime.now());
            userEntity.setSubscriptionType("FREE");
            UserModel finalModel = userRepository.save(userEntity);

            // UserNutritionPlan userNutritionPlan = UserNutritionPlan.builder()
            // .userId(finalModel.getId())
            // .calories(BigDecimal.ZERO)
            // .fats(BigDecimal.ZERO)
            // .carbohydrates(BigDecimal.ZERO)
            // .proteins(BigDecimal.ZERO)
            // .build();
            // userNutritionPlanRepository.save(userNutritionPlan);
            return new BaseResponse();
        } catch (Exception e) {
            log.error(e, e);
            return BaseResponse
                    .builder()
                    .httpStatusCode("400")
                    .description(e.getMessage()).build();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserReq req) throws JsonProcessingException {
        // TODO: process POST request
        try {
            // go to UserDetailServiceImpl.loadByUsername
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(),
                            req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Object jwt = jwtUtil.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtModel(jwt,
                    userDetails.getUserModel().getId(),
                    userDetails.getUserModel().getUsername(),
                    userDetails.getUserModel().getSubscriptionType()));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode("400")
                    .description(e.getMessage()).build());
        }

    }
}
