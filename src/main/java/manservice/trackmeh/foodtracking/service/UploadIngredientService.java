package manservice.trackmeh.foodtracking.service;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UploadIngredientService {
    public void exportTemplate(HttpServletRequest req,HttpServletResponse res);
    public void importIngredientData(MultipartFile file, HttpServletRequest req, HttpServletResponse res);
    // public void saveList(List<Ingredient> )
}
