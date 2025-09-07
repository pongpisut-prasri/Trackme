package manservice.trackmeh.foodtracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.PermissionReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.service.PermissionService;
import manservice.trackmeh.utils.Constant.HTTP_RESPONSE;


@Log4j2
@RestController
@RequestMapping("/demo/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<?> createPermission(@RequestBody PermissionReq req) {
         try {
            return ResponseEntity.ok().body(permissionService.CreatePermission(req));
        } catch (Exception e) {
            log.error(e, e);
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }
    }


    @PostMapping("/import")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description("File is empty").build());
        }
        
        try {
            return ResponseEntity.ok().body(permissionService.importFromExcel(file));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(BaseResponse
                    .builder()
                    .httpStatusCode(HTTP_RESPONSE.BAD_REQUEST.code())
                    .description(e.getMessage()).build());
        }
    }
    
}
