package manservice.trackmeh.foodtracking.service;

import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.security.UserDetailsImpl;
import manservice.trackmeh.foodtracking.security.jwt.JwtModel;

public interface UserService {
    public UserModel getUserModelByUsername(String username);

    public UserModel createUser(UserReq req);

    public void updateUserJwt(String userId, String jwt);

    public JwtModel login(UserReq req) throws Exception;
}
