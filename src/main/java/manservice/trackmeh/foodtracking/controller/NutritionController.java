package manservice.trackmeh.foodtracking.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.BasePaginationReq;
import manservice.trackmeh.foodtracking.dto.request.NutritionLogsReq;
import manservice.trackmeh.foodtracking.dto.request.NutritionPlanReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.service.UserNutritionService;
import manservice.trackmeh.utils.Constant.HTTP_RESPONSE;

@Log4j2
@RestController
@RequestMapping("/demo/nutrition")
public class NutritionController {

    @Autowired
    UserNutritionService userNutritionService;

    @PostMapping("/planSetup")
    public ResponseEntity<?> nutritionPlanSetup(@RequestHeader("Authorization") String token,
            @Valid @RequestBody NutritionPlanReq req) {
        try {

            return ResponseEntity.ok().body(userNutritionService.nutritionPlanSetup(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }

    @PostMapping("/nutritionLogs")
    public ResponseEntity<?> nutritionLogs(@RequestHeader("Authorization") String token,
            @Valid @RequestBody NutritionLogsReq req) {
        try {

            return ResponseEntity.ok().body(userNutritionService.nutritionLogs(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }

    @PostMapping("/dailyNutritionSummary")
    public ResponseEntity<?> dailyNutritionSummary(@RequestHeader("Authorization") String token,
            @Valid @RequestBody NutritionLogsReq req) {
        try {
            LocalDate date = Optional.ofNullable(req.getLogDate()).orElse(LocalDate.now());
            return ResponseEntity.ok().body(userNutritionService.getDailySummaryNutrition(req.getUserId(), date));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }

    @PostMapping("/getLogsPagination")
    public ResponseEntity<?> getLogsPagination(@RequestHeader("Authorization") String token,
            @RequestBody BasePaginationReq req) {
        try {
            return ResponseEntity.ok().body(userNutritionService.getLogsPagination(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }
}
