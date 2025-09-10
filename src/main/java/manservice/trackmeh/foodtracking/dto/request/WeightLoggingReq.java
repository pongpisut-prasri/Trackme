package manservice.trackmeh.foodtracking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class WeightLoggingReq extends BaseRequest {
    private LocalDate date;
    private BigDecimal weightKg;
    private BigDecimal bodyFatPercent;

    @Getter
    @Setter
    @SuperBuilder
    public static class WeightLogListByDateReq extends BaseRequest{
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
