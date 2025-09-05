package manservice.trackmeh.foodtracking.service;

import manservice.trackmeh.foodtracking.dto.request.WeightLoggingReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;

public interface BodyLogsService {
    public BaseResponse weightLogging(WeightLoggingReq req);
}
