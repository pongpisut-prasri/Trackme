package manservice.trackmeh.foodtracking.service.poi.impl;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import manservice.trackmeh.foodtracking.entity.Ingredient;
import manservice.trackmeh.foodtracking.repository.IngredientRepository;
import manservice.trackmeh.foodtracking.service.UploadIngredientService;
import manservice.trackmeh.foodtracking.service.poi.template.TemplateIngredient;

@Log4j2
@Service
public class UploadIngredientServiceImpl implements UploadIngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    @SneakyThrows
    public void exportTemplate(HttpServletRequest req, HttpServletResponse res) {
        TemplateIngredient template = new TemplateIngredient(req);
        template.exportFile(res);
    }

    @Override
    public void importIngredientData(MultipartFile file, HttpServletRequest req, HttpServletResponse res) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet worksheet = workbook.getSheet("INGREDIENT");
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Ingredient entity = new Ingredient();
                XSSFRow row = worksheet.getRow(i);
                Iterator<Cell> cellIte = row.cellIterator();
                Cell ingredientCell = cellIte.next();
                Cell ingredientCellEn = cellIte.next();
                entity.setIngredientName((String) getValueFromCell(ingredientCell));
                entity.setIngredientNameEn((String) getValueFromCell(ingredientCellEn));
                while (cellIte.hasNext()) {
                    Cell cell = cellIte.next();
                    String valueFromCell = (String) getValueFromCell(cell);
                    switch (cell.getColumnIndex()) {
                        case 2:
                            entity.setProtein(new BigDecimal(valueFromCell));
                            break;
                        case 3:
                            entity.setCarbohydrate(new BigDecimal(valueFromCell));
                            break;
                        case 4:
                            entity.setFat(new BigDecimal(valueFromCell));
                            break;

                        default:
                            break;
                    }
                }
                ingredientRepository.save(entity);

            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void saveData() {

    }

    private <T> T getValueFromCell(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return (T) cell.getStringCellValue();
            case BLANK:
                return (T) cell.getStringCellValue();
            case NUMERIC:
                return (T) String.valueOf(cell.getNumericCellValue());
            default:
                throw new IllegalArgumentException();
        }
    }

}
