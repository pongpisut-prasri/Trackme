package manservice.trackmeh.foodtracking.dto.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryPaginationReq extends BasePaginationReq{
    private LocalDate startDate;
    private LocalDate endDate;
}
