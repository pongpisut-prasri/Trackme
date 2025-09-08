package manservice.trackmeh.foodtracking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePaginationReq extends BaseRequest {
    private int page;
    private int size;
}
