package manservice.trackmeh.foodtracking.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;
import manservice.trackmeh.foodtracking.dto.request.NutritionLogsReq;
import manservice.trackmeh.foodtracking.dto.request.NutritionPlanReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.entity.UserNutritionLogs;
import manservice.trackmeh.foodtracking.entity.UserNutritionPlan;
import manservice.trackmeh.foodtracking.repository.UserNutritionLogsRepository;
import manservice.trackmeh.foodtracking.repository.UserNutritionLogsRepository.UserNutritionGroupByUserAndLogDate;
import manservice.trackmeh.foodtracking.repository.UserNutritionPlanRepository;
import manservice.trackmeh.foodtracking.service.UserNutritionService;

@Service
@Transactional
public class UserNutritionServiceImpl implements UserNutritionService {

    @Autowired
    private UserNutritionLogsRepository userNutritionLogsRepository;

    @Autowired
    private UserNutritionPlanRepository userNutritionPlanRepository;

    @Override
    public BaseResponse nutritionLogs(NutritionLogsReq req) {
        UserNutritionLogs entity = new UserNutritionLogs();
        BeanUtils.copyProperties(req, entity);
        if (req.getLogDate() == null) {
            entity.setLogDate(LocalDate.now());
        }
        entity.setUserId(req.getUserId());
        entity.setCalories(calculateCalories(req.getProteins(), req.getCarbohydrates(), req.getFats()));
        entity.setCreateDate(LocalDateTime.now());
        UserNutritionLogs save = userNutritionLogsRepository.save(entity);
        return getDailySummaryNutrition(req.getUserId(), save.getLogDate());
    }

    @Override
    @SneakyThrows
    @Transactional(readOnly = true)
    public BaseResponse getDailySummaryNutrition(String userId, LocalDate date) {

        UserNutritionGroupByUserAndLogDate dailySummary = userNutritionLogsRepository.getDailySummary(userId,
                date);

        UserNutritionPlan nutritionPlan = userNutritionPlanRepository.findByUserId(userId);
        BigDecimal remainingCalories = BigDecimal.ZERO;
        BigDecimal remainingCarb = BigDecimal.ZERO;
        BigDecimal remainingProtein = BigDecimal.ZERO;
        BigDecimal remainingFat = BigDecimal.ZERO;
        BigDecimal weight = BigDecimal.ZERO;

        BigDecimal eatenCalories = BigDecimal.ZERO;
        BigDecimal eatenCarb = BigDecimal.ZERO;
        BigDecimal eatenProtein = BigDecimal.ZERO;
        BigDecimal eatenFat = BigDecimal.ZERO;
        if (dailySummary != null) {
            remainingCalories = nutritionPlan.getCalories().subtract(dailySummary.getTotalCalories());
            remainingCarb = nutritionPlan.getCarbohydrates().subtract(dailySummary.getTotalCarbohydrates());
            remainingProtein = nutritionPlan.getProteins().subtract(dailySummary.getTotalProteins());
            remainingFat = nutritionPlan.getFats().subtract(dailySummary.getTotalFats());
            eatenCalories = dailySummary.getTotalCalories();
            eatenCarb = dailySummary.getTotalCarbohydrates();
            eatenProtein = dailySummary.getTotalProteins();
            eatenFat = dailySummary.getTotalFats();
            weight = dailySummary.getWeight();
        }
        Map<String, Map<String, BigDecimal>> response = new HashMap<>();
        response.put("todayWeight",
                Map.of("weight", weight));
        response.put("eaten",
                Map.of("protein", eatenProtein,
                        "fat", eatenFat,
                        "carbohydrate", eatenCarb,
                        "calories", eatenCalories));
        response.put("remaining",
                Map.of("protein", remainingProtein,
                        "fat", remainingFat,
                        "carbohydrate", remainingCarb,
                        "calories", remainingCalories));
        return new BaseResponse(response);
    }

    @Override
    @SneakyThrows
    public BaseResponse nutritionPlanSetup(NutritionPlanReq req) {
        UserNutritionPlan userNutritionPlan = userNutritionPlanRepository.findByUserId(Optional.ofNullable(req.getUserId()).orElse(""));
        if (userNutritionPlan == null) {
            userNutritionPlan = new UserNutritionPlan();
            BeanUtils.copyProperties(req, userNutritionPlan);
            userNutritionPlan.setCreateDate(LocalDateTime.now());
        } else {
            userNutritionPlan.setCarbohydrates(req.getCarbohydrates());
            userNutritionPlan.setFats(req.getFats());
            userNutritionPlan.setProteins(req.getProteins());
            userNutritionPlan.setUpdateDate(LocalDateTime.now());
        }
        userNutritionPlan.setCalories(calculateCalories(req.getProteins(), req.getCarbohydrates(), req.getFats()));
        userNutritionPlanRepository.save(userNutritionPlan);
        return new BaseResponse();
    }

    private BigDecimal calculateCalories(BigDecimal protein, BigDecimal carbohydrate, BigDecimal fats) {
        BigDecimal proteinCalories = protein.multiply(BigDecimal.valueOf(4));
        BigDecimal carbohydrateCalories = carbohydrate.multiply(BigDecimal.valueOf(4));
        BigDecimal fatCalories = fats.multiply(BigDecimal.valueOf(9));
        return (proteinCalories.add(carbohydrateCalories).add(fatCalories)).setScale(4, RoundingMode.HALF_UP);
    }

}
