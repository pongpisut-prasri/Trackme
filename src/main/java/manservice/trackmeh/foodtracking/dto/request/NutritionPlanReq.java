package manservice.trackmeh.foodtracking.dto.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NutritionPlanReq {
    private String userNutritionPlanId;
    private String userId;
    private String nutritionPlanName;
    private BigDecimal proteins;
    private BigDecimal carbohydrates;
    private BigDecimal fats;
    private BigDecimal calories;
}
