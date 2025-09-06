package manservice.trackmeh.foodtracking.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
        UserBodyLogs entity = new UserBodyLogs();

        BeanUtils.copyProperties(req, entity);
        entity.setMeasuredAt(Optional.ofNullable(req.getDate()).orElse(LocalDate.now()));
        entity.setUserId(req.getUserId());
        entity.setCreateDate(LocalDateTime.now());
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
