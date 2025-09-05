package manservice.trackmeh.foodtracking.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionLogsReq extends BaseRequest {
    private String userNutritionLogsId;
    private String userNutritionPlanId;
    private String userId;
    private String foodName;
    private BigDecimal proteins;
    private BigDecimal carbohydrates;
    private BigDecimal fats;
    private BigDecimal calories;
    private LocalDate logDate;
}
