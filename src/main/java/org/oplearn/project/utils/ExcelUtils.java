/*
package org.oplearn.project.utils;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {
  private ExcelUtils() {
  }

  public static CellStyle createBorderStyleCenter(Workbook workbook) {
    CellStyle borderStyle = workbook.createCellStyle();
    borderStyle.setBorderTop(BorderStyle.THIN);
    borderStyle.setBorderBottom(BorderStyle.THIN);
    borderStyle.setBorderLeft(BorderStyle.THIN);
    borderStyle.setBorderRight(BorderStyle.THIN);
    borderStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa
    return borderStyle;
  }

  // Hàm tạo border style với căn trái
  public static CellStyle createBorderStyleLeft(Workbook workbook) {
    CellStyle borderStyle = workbook.createCellStyle();
    borderStyle.setBorderTop(BorderStyle.THIN);
    borderStyle.setBorderBottom(BorderStyle.THIN);
    borderStyle.setBorderLeft(BorderStyle.THIN);
    borderStyle.setBorderRight(BorderStyle.THIN);
    borderStyle.setAlignment(HorizontalAlignment.LEFT); // Căn trái
    return borderStyle;
  }

  public static void fillCell(Sheet sheet, int rowIndex, int cellIndex, String value) {
    Row row = sheet.getRow(rowIndex);
    if (row == null) row = sheet.createRow(rowIndex);
    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    cell.setCellValue(value != null ? value : "");
  }
}
*/
