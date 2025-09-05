package manservice.trackmeh.foodtracking.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@Log4j2
@RestController
@RequestMapping("/demo/auth")
public class AuthController {

    @Autowired
    private UserModelRepository userRepository;

    // @Autowired
    // private UserNutritionPlanRepository userNutritionPlanRepository;

    // @Autowired
    // private JwtUtil jwtUtil;

    // @Autowired
    // private AuthenticationManager authenticationManager;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public BaseResponse register(@Valid @RequestBody UserReq req) {
        try {
            // String passwordEncrypted = passwordEncoder.encode(req.getPassword());
            UserModel userEntity = new UserModel();
            userEntity.setUsername(req.getUsername());
            userEntity.setPasswordEncrypted(req.getPassword());
            userEntity.setCreateDate(LocalDateTime.now());
            UserModel finalModel = userRepository.save(userEntity);

            // UserNutritionPlan userNutritionPlan = UserNutritionPlan.builder()
            //         .userId(finalModel.getId())
            //         .calories(BigDecimal.ZERO)
            //         .fats(BigDecimal.ZERO)
            //         .carbohydrates(BigDecimal.ZERO)
            //         .proteins(BigDecimal.ZERO)
            //         .build();
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
    public String login(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }
}
