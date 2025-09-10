package manservice.trackmeh.foodtracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.dto.request.BaseRequest;
import manservice.trackmeh.foodtracking.service.UploadIngredientService;

@Log4j2
@RestController
@RequestMapping("/demo/file")
public class UploadFileController {
    @Autowired
    UploadIngredientService uploadIngredientService;

    @PostMapping(value = "/exportIngredientTemplate")
    public void exportIngredientTemplate(@RequestBody BaseRequest file, HttpServletRequest request,
            HttpServletResponse res) {
        try {
            uploadIngredientService.exportTemplate(request, res);
        } catch (Exception e) {
            log.error(e, e);
        }

    }

    @PostMapping(value = "/importIngredient", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importIngredient(@ModelAttribute MultipartFile file, HttpServletRequest request,
            HttpServletResponse res) {
        try {
            uploadIngredientService.importIngredientData(file, request, res);
        } catch (Exception e) {
            log.error(e, e);
        }

    }
}
