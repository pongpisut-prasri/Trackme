package manservice.trackmeh.foodtracking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeightLoggingReq extends BaseRequest {
    private LocalDate date;
    private BigDecimal weightKg;
    private BigDecimal bodyFatPercent;
}
