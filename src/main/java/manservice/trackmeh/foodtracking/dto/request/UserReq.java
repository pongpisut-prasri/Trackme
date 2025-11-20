package manservice.trackmeh.foodtracking.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Valid
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserReq extends BaseRequest {
    @NotEmpty(message = "{notEmpty.message}")
    private String username;
    @NotEmpty(message = "{notEmpty.message}")
    private String password;
}
