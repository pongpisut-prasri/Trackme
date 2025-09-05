package manservice.trackmeh.foodtracking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class UserReq extends BaseRequest {
    @NotEmpty(message = "{notEmpty.message}")
    private String username;
    @NotEmpty(message = "{notEmpty.message}")
    private String password;
}
