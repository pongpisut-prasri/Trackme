package manservice.trackmeh.foodtracking.service;

import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.entity.UserModel;

public interface UserService {
    public UserModel getUserModelByUsername(String username);
    public UserModel createUser(UserReq req);
}
