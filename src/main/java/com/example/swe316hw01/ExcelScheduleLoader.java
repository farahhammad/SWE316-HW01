package com.example.swe316hw01;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;

/**
 * Single responsibility: Load the Excel file and convert rows into Section objects.
 * Stateless: holds no internal data after load().
 */
public class ExcelScheduleLoader {

    /**
     * Reads the first sheet of the Excel file and returns all sections.
     * Reuses Building instances via a local cache to avoid duplicates.
     */
    public List<Section> load(String filePath) {
        List<Section> allSections = new ArrayList<>();
        Map<String, Building> buildingsCache = new HashMap<>();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            System.out.println("===============================================");
            System.out.println("Reading Excel data and creating Section objects...");
            System.out.println("===============================================");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String crn            = getValue(row, 1);
                String courseCode     = getValue(row, 2);
                String deptName       = getValue(row, 3);
                String sectionNumber  = getValue(row, 4);
                String courseTitle    = getValue(row, 5);
                String days           = getValue(row, 7);
                String startTime      = getValue(row, 8);
                String endTime        = getValue(row, 9);
                String buildingNumber = getValue(row, 10);
                String roomNumber     = getValue(row, 11);
                String instructor     = getValue(row, 12);

                if (crn.isEmpty()) continue;

                // Create or reuse Building from cache (your Building sets coordinates internally)
                Building building = null;
                if (!buildingNumber.isEmpty()) {
                    building = buildingsCache.computeIfAbsent(buildingNumber, Building::new);
                }

                Room room = null;
                if (!roomNumber.isEmpty() && building != null) {
                    room = new Room(roomNumber, building);
                }

                Meeting meeting = new Meeting(
                        days.isEmpty()      ? "N/A" : days,
                        startTime.isEmpty() ? "N/A" : startTime,
                        endTime.isEmpty()   ? "N/A" : endTime,
                        room,
                        building
                );

                // Section(departmentName, courseTitle, courseName, crn, sectionNumber, type, meeting, instructor)
                Section section = new Section(
                        deptName.isEmpty()      ? "Unknown" : deptName,
                        courseCode.isEmpty()    ? "N/A"     : courseCode,
                        courseTitle.isEmpty()   ? "N/A"     : courseTitle,
                        crn,
                        sectionNumber.isEmpty() ? "N/A"     : sectionNumber,
                        "LEC",
                        meeting,
                        instructor.isEmpty()    ? "TBA"     : instructor
                );

                allSections.add(section);
                rowCount++;

                if (rowCount % 100 == 0) {
                    System.out.println("Processed " + rowCount + " sections...");
                }
            }

            System.out.println("===============================================");
            System.out.println("Total Sections created: " + rowCount);
            System.out.println("===============================================");

        } catch (Exception e) {
            System.err.println("ERROR reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }

        return allSections;
    }

    /**
     * Reads a cell as string. Numeric cells are cast to int then to String
     * (e.g., 800 not 0800), which is fine for our time parsing.
     */
    private static String getValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        String value;
        switch (cell.getCellType()) {
            case STRING -> value = cell.getStringCellValue();
            case NUMERIC -> value = String.valueOf((int) cell.getNumericCellValue());
            default -> value = "";
        }
        return value.trim();
    }
}
