package manservice.trackmeh.foodtracking.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq;
import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq.WeightLogListByDateReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.UserBodyLogs;
import manservice.trackmeh.foodtracking.entity.UserNutritionLogs;
import manservice.trackmeh.foodtracking.repository.UserBodyLogsRepository;
import manservice.trackmeh.foodtracking.service.BodyLogsService;

@Service
@Transactional
public class BodyLogsServiceImpl implements BodyLogsService {

    @Autowired
    UserBodyLogsRepository userBodyLogsRepository;

    @Override
    public BaseResponse weightLogging(WeightLoggingReq req) {
        // Find if a log already exists for this userId and date
        if (StringUtils.isEmpty(req.getUserId())&&req.getWeightKg() == null) {
            throw new IllegalArgumentException("userId/weight must not be null or empty");
        }
        if (StringUtils.isEmpty(req.getUserId())) {
            throw new IllegalArgumentException("userId must not be null or empty");
        }
        if (req.getWeightKg() == null) {
            throw new IllegalArgumentException("weight must not be null");
        }
        UserBodyLogs entity = new UserBodyLogs();
        Optional<UserBodyLogs> existingLogOpt = userBodyLogsRepository
                .findByMeasuredAtAndUserId(Optional.ofNullable(req.getDate()).orElse(LocalDate.now()),req.getUserId());
        BeanUtils.copyProperties(req, entity);
        if (existingLogOpt.isPresent()) {
            entity = existingLogOpt.get();
            entity.setUpdateDate(LocalDateTime.now());
        }else{
            entity.setCreateDate(LocalDateTime.now());
            entity.setUserId(req.getUserId());
        }
        entity.setMeasuredAt(Optional.ofNullable(req.getDate()).orElse(LocalDate.now()));
        userBodyLogsRepository.save(entity);
        return new BaseResponse();
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse getWeightLogInRangeDate(WeightLogListByDateReq req) {
        return new BaseResponse(
                userBodyLogsRepository.getLogsInRangeDate(req.getUserId(), req.getStartDate(), req.getEndDate()));
    }

}
