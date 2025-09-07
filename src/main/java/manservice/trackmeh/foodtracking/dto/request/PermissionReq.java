package manservice.trackmeh.foodtracking.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionReq extends BaseRequest {
    private String subscriptionType;
    private List<String> permission;
}
