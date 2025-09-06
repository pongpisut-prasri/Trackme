package manservice.trackmeh.foodtracking.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.UserReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq.WeightLogListByDateReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.UserModel;
import manservice.trackmeh.foodtracking.service.BodyLogsService;

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
                    .httpStatusCode("400")
                    .description(e.getMessage()).build());
        }

    }

    @PostMapping("/getLogsInRange")
    public ResponseEntity<?> getLogsInRange(@Valid @RequestBody WeightLogListByDateReq req) {
        try {
            return ResponseEntity.ok().body(bodyLogsService.getWeightLogInRangeDate(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode("400")
                    .description(e.getMessage()).build());
        }

    }
}
