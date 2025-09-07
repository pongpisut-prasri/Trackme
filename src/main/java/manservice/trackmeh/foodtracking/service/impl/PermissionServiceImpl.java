package manservice.trackmeh.foodtracking.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import manservice.trackmeh.foodtracking.dto.request.PermissionReq;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.entity.Permission;
import manservice.trackmeh.foodtracking.repository.PermissionRepository;
import manservice.trackmeh.foodtracking.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public BaseResponse CreatePermission(PermissionReq req) {
        Permission entity = new Permission();
        BeanUtils.copyProperties(req, entity);
        entity.setCreateBy("ADMIN");
        entity.setCreateDate(LocalDateTime.now());
        permissionRepository.save(entity);
        return new BaseResponse();
    }

     public BaseResponse importFromExcel(MultipartFile file) throws IOException {
        List<Permission> permissions = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            if (isRowEmpty(row)) {
                continue;
            }

            Permission permission = new Permission();
            
            Cell cellSubscriptionType = row.getCell(0);
            if (cellSubscriptionType != null) {
                permission.setSubscriptionType(cellSubscriptionType.getStringCellValue());
            }

            Cell cellPermissions = row.getCell(1);
            if (cellPermissions != null) {
                String permissionsString = cellPermissions.getStringCellValue();
                List<String> permissionList = Arrays.asList(permissionsString.split(",\\s*"));
                permission.setPermission(permissionList);
            }
            
            permissions.add(permission);
        }

        workbook.close();
        permissionRepository.saveAll(permissions);
        return new BaseResponse();
    }
    
    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}


