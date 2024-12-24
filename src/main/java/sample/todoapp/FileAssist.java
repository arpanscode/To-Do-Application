package sample.todoapp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class FileAssist {
    public static ObservableList<ToDoItem> readFile(String file_path) {
        String category = null, description = null, status = null;
        ArrayList<String> categories = new ArrayList<String>();
        ObservableList<ToDoItem> toDoItems = FXCollections.observableArrayList();
        if (!file_path.isEmpty()) {
            try {
                FileInputStream excel = new FileInputStream(file_path);
                XSSFWorkbook workbook = new XSSFWorkbook(excel);

                // read the to-do items
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next(); // get rid of the header line

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {

                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                category = cell.getStringCellValue();
                                break;
                            case 1:
                                description = cell.getStringCellValue();
                                break;
                            case 2:
                                status = cell.getStringCellValue();
                                break;
                        }
                    }
                    toDoItems.add(new ToDoItem(category,description,status));


                }

                // read the settings:
                XSSFSheet settings_sheet = workbook.getSheetAt(1);
                Iterator<Row> settings_rowIterator = settings_sheet.iterator();
                settings_rowIterator.next();
                while (settings_rowIterator.hasNext()) {
                    Row settings_row = settings_rowIterator.next();
                    Iterator<Cell> settings_cellIterator = settings_row.cellIterator();
                    while (settings_cellIterator.hasNext()) {
                        Cell cell = settings_cellIterator.next();
                        categories.add(cell.getStringCellValue());
                    }
                }
                String[] categories_arr = categories.toArray(new String[0]);
                Settings.setCategories(categories_arr);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return toDoItems;
    }

    public static void writeFile(ObservableList<ToDoItem> toDoItems, String file_path)
    {
        String[] categories = Settings.getCategories();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Item Details");

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(boldFont);

        int rownum = 0;
        int colnum;

        Row row = sheet.createRow(rownum);
        for(colnum = 0; colnum < 3; colnum++) {
            switch (colnum) {
                case 0:
                    Cell cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Category");
                    sheet.autoSizeColumn(colnum);
                    break;
                case 1:
                    cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Description");
                    sheet.autoSizeColumn(colnum);
                    break;
                case 2:
                    cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Status");
                    sheet.autoSizeColumn(colnum);
                    break;
            }
        }
        colnum = 0;
        rownum++;

        for (ToDoItem item : toDoItems) {
            row = sheet.createRow(rownum);
            for(colnum = 0; colnum < 3; colnum++) {
                switch (colnum) {
                    case 0:
                        Cell cell = row.createCell(colnum);
                        cell.setCellValue(item.getCategory());
                        sheet.autoSizeColumn(colnum);
                        break;
                    case 1:
                        cell = row.createCell(colnum);
                        cell.setCellValue(item.getDescription());
                        sheet.autoSizeColumn(colnum);
                        break;
                    case 2:
                        cell = row.createCell(colnum);
                        cell.setCellValue(item.getStatus());
                        sheet.autoSizeColumn(colnum);
                        break;
                }
            }
            colnum = 0;
            rownum++;
        }

        // write settings page
        XSSFSheet settings_sheet = workbook.createSheet("Settings");
        rownum = 0;
        colnum = 0;

        row = settings_sheet.createRow(rownum);
        Cell cell = row.createCell(colnum);
        cell.setCellStyle(style);
        cell.setCellValue("Category");
        settings_sheet.autoSizeColumn(colnum);
        rownum++;

        for(String cat : categories)
        {
            row = settings_sheet.createRow(rownum);
            cell = row.createCell(colnum);
            cell.setCellValue(cat);
            sheet.autoSizeColumn(colnum);
            rownum++;
        }

        try {
            // Writing the workbook
            FileOutputStream out = new FileOutputStream(new File(file_path));
            workbook.write(out);
            out.close();
        }catch (Exception e) {
        }
    }

    public static void downloadTemplate(String file_path)
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Item Details");

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(boldFont);

        int rownum = 0;
        int colnum = 0;

        Row row = sheet.createRow(rownum);
        for(colnum = 0; colnum < 3; colnum++) {
            switch (colnum) {
                case 0:
                    Cell cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Category");
                    sheet.autoSizeColumn(colnum);
                    break;
                case 1:
                    cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Description");
                    sheet.autoSizeColumn(colnum);
                    break;
                case 2:
                    cell = row.createCell(colnum);
                    cell.setCellStyle(style);
                    cell.setCellValue("Status");
                    sheet.autoSizeColumn(colnum);
                    break;
            }
        }

        // writing settings page
        XSSFSheet settings_sheet = workbook.createSheet("Settings");
        rownum = 0;
        colnum = 0;

        row = settings_sheet.createRow(rownum);
        Cell cell = row.createCell(colnum);
        cell.setCellStyle(style);
        cell.setCellValue("Category");
        settings_sheet.autoSizeColumn(colnum);

        try {
            // Writing the workbook
            FileOutputStream out = new FileOutputStream(new File(file_path));
            workbook.write(out);
            out.close();
        }catch (Exception e) {
        }
    }
}
