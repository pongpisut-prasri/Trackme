package manservice.trackmeh.foodtracking.service;

import java.time.LocalDate;

import manservice.trackmeh.foodtracking.dto.request.NutritionLogsReq;
import manservice.trackmeh.foodtracking.dto.request.NutritionPlanReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;

public interface UserNutritionService {
    public BaseResponse nutritionLogs(NutritionLogsReq req);

    public BaseResponse nutritionPlanSetup(NutritionPlanReq req);

    public BaseResponse getDailySummaryNutrition(String userId, LocalDate date);
}
