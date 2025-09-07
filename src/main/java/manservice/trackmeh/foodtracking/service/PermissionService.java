package manservice.trackmeh.foodtracking.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import manservice.trackmeh.foodtracking.dto.request.PermissionReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.Permission;

public interface PermissionService {
    public BaseResponse CreatePermission(PermissionReq req);

    public BaseResponse importFromExcel(MultipartFile file) throws IOException;
}
