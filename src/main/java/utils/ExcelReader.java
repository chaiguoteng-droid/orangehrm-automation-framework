package utils;

import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    public static Object[][] getTestData(String sheetName) {
        FileInputStream file = null;
        XSSFWorkbook workbook = null;
        Object[][] data = null;

        try {
            file = new FileInputStream("./src/test/resources/testdata.xlsx");
            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            
            int rows = sheet.getLastRowNum();
            int cols = sheet.getRow(0).getLastCellNum();
            data = new Object[rows][cols];
            DataFormatter formatter = new DataFormatter();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i][j] = formatter.formatCellValue(sheet.getRow(i + 1).getCell(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}