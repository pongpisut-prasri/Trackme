package manservice.trackmeh.foodtracking.service.poi.template;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.SheetUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manservice.trackmeh.utils.Constant.INGREDIENT_CELL;

public class TemplateIngredient {
    public static final String BILLION_COMMA_BIG_DECIMAL_FORMAT = "#,###,###,##0.00";
    public static final String TEXT_FORMAT = "@";
    public static final String D_MONTH_YYYY = "d MMMM yyyy";
    public static final String DDMMYYYY = "dd/MM/yyyy";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String DDMMYYYY_HHMMSS = "dd-MM-yyyy HH:mm:ss";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    public static final String DDMMMYYYY = "dd-MMM-yyyy";
    public static final String DDMMMYYYY_HHMMSS = "dd-MMM-yyyy HH:mm:ss";
    public static final String FONT_NAME_CORDIA_NEW = "Cordia New";
    public final String SHEET_NAME = "INGREDIENT";
    // resource
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private HttpServletRequest request;
    private Integer rowCount = 0;

    public void autoSizeColumn(int column) {
        // this.setColumnWidth(column, sheet.getColumnWidth(column) + 800);
        this.autoSizeColumn(column, false);
    }

    public void autoSizeColumn(int column, boolean useMergedCells) {
        double width = SheetUtil.getColumnWidth(this.sheet, column, useMergedCells);
        if (width != -1.0) {
            width *= (256.0);
            int maxColumnWidth = '\uff00';
            if (width > (double) maxColumnWidth) {
                width = (double) maxColumnWidth;
            }

            this.setColumnWidth(column, Math.toIntExact(Math.round(width)));
            this.sheet.getColumnHelper().setColBestFit((long) column, true);
        }
        int MIN_WIDTH = 20;

        if (sheet.getColumnWidth(column) < (247 * MIN_WIDTH)) { // 256 is the unit POI uses per character
            sheet.setColumnWidth(column, 247 * MIN_WIDTH);
        }

    }

    public void setColumnWidth(int columnIndex, int width) {
        if (width > 65280) {
            throw new IllegalArgumentException("The maximum column width for an individual cell is 255 characters.");
        } else {
            this.sheet.getColumnHelper().setColWidth((long) columnIndex, (double) width / 256.0);
            this.sheet.getColumnHelper().setCustomWidth((long) columnIndex, true);
        }
    }

    // create blank sheet
    public TemplateIngredient(HttpServletRequest request) {
        this.workbook = new XSSFWorkbook();
        this.request = request;
        this.sheet = workbook.createSheet(this.SHEET_NAME);
    }

    public void writeHeaderLines() {
        XSSFDataFormat dataFormat = this.workbook.createDataFormat();

        // header style
        XSSFCellStyle headerBorderStyle = cellBorderACenter(dataFormat, fontBold());
        String headerBGColorHex = "9BC2E6";
        XSSFColor headerBGColor = new XSSFColor();
        headerBGColor.setARGBHex(headerBGColorHex);
        headerBorderStyle.setFillForegroundColor(headerBGColor);
        headerBorderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerBorderStyle.setWrapText(false);

        rowCount = 0;
        int columnNumber = 0;
        Row headerRow = sheet.createRow(rowCount);
        List<INGREDIENT_CELL> headerColumnList = List.of(INGREDIENT_CELL.values());
        // generate header column
        for (INGREDIENT_CELL columnInfo : headerColumnList) {
            Cell column = headerRow.createCell(columnNumber);
            column.setCellType(CellType.STRING);
            column.setCellStyle(headerBorderStyle);
            column.setCellValue(columnInfo.getCellName());
            columnNumber++;
        }
        rowCount++;
        for (int i = 0; i < headerColumnList.size(); i++) {
            this.autoSizeColumn(i);
        }

    }

    public void exportFile(HttpServletResponse response) throws IOException {
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + "Template" + ".xlsx";
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(headerKey, headerValue);

        writeHeaderLines();

        double originalRatio = ZipSecureFile.getMinInflateRatio();
        ZipSecureFile.setMinInflateRatio(0.0001);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {

        } finally {
            ZipSecureFile.setMinInflateRatio(originalRatio);
        }
    }

    private Font fontBoldFalse() {
        Font font = this.workbook.createFont();
        font.setBold(Boolean.FALSE);
        font.setFontName(FONT_NAME_CORDIA_NEW);
        font.setFontHeightInPoints((short) 14);
        return font;
    }

    private Font fontBold() {
        String hexColor = "FFFFFF";
        XSSFColor color = new XSSFColor();
        color.setARGBHex(hexColor);
        Font font = this.workbook.createFont();
        font.setColor(color.getIndex());
        font.setBold(Boolean.TRUE);
        font.setFontName(FONT_NAME_CORDIA_NEW);
        font.setFontHeightInPoints((short) 16);
        return font;
    }

    private XSSFCellStyle cellBorderAllACenterFMText(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setDataFormat(dataFormat.getFormat(TEXT_FORMAT));
        cellStyle.setFont(font);
        // cellStyle.setFillForegroundColor(Short.valueOf("#DDEBF7"));
        return cellStyle;
    }

    private XSSFCellStyle cellBorderAllACenterFMTextBorderless(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setDataFormat(dataFormat.getFormat(TEXT_FORMAT));
        cellStyle.setFont(font);
        // cellStyle.setFillForegroundColor(Short.valueOf("#DDEBF7"));
        return cellStyle;
    }

    private XSSFCellStyle cellBorderAllALeftFMText(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setDataFormat(dataFormat.getFormat(TEXT_FORMAT));
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle cellBorderAllACenterFMDate(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setDataFormat(dataFormat.getFormat(DDMMYYYY));
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle cellBorderAllACenterFMDateTime(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setDataFormat(dataFormat.getFormat(DDMMYYYYHHMMSS));
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle cellBorderAllARightFMNum(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setDataFormat(dataFormat.getFormat(BILLION_COMMA_BIG_DECIMAL_FORMAT));
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle cellBorderACenter(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // cellStyle.setWrapText(true);
        // cellStyle.setFillForegroundColor(Short.valueOf("#DDEBF7"));

        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle cellBorderLessACenter(DataFormat dataFormat, Font font) {
        XSSFCellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
