package manservice.trackmeh.foodtracking.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq.WeightLogListByDateReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.service.BodyLogsService;
import manservice.trackmeh.utils.Constant.HTTP_RESPONSE;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RestController
@RequestMapping("/demo/bodyLogs")
public class BodyLogsController {

    @Autowired
    BodyLogsService bodyLogsService;

    @PostMapping("/weight")
    public ResponseEntity<?> register(@Valid @RequestBody WeightLoggingReq req) {
        try {
            return ResponseEntity.ok().body(bodyLogsService.weightLogging(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }

    @GetMapping("/getLogsInRange")
    public ResponseEntity<?> getLogsInRange(@RequestHeader("Authorization") String token,
            @RequestParam String userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
            ) {
        try {
            return ResponseEntity.ok().body(bodyLogsService.getWeightLogInRangeDate(WeightLogListByDateReq.builder()
                    .userId(userId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build()));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }

    }
}
