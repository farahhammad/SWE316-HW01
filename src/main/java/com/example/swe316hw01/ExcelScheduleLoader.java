package com.example.swe316hw01;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;

/**
 * Loads the Excel file and converts rows into Section objects.
 * Stateless: holds no internal data after load().
 */
public class ExcelScheduleLoader {

    public List<Section> load(String filePath) {
        List<Section> allSections = new ArrayList<>();
        Map<String, Building> buildingsCache = new HashMap<>();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            System.out.println("Reading Excel data and creating Section objects...");

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

                Department dept = new Department(deptName.isEmpty() ? "Unknown" : deptName);
                Course course = new Course(
                        courseCode.isEmpty()  ? "N/A" : courseCode,
                        courseTitle.isEmpty() ? "N/A" : courseTitle,
                        dept
                );

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
                        room
                );

                // now using String type instead of enum
                Section section = new Section(
                        crn,
                        sectionNumber.isEmpty() ? "N/A" : sectionNumber,
                        "LEC", // default type
                        meeting,
                        instructor.isEmpty() ? "TBA" : instructor,
                        course
                );

                allSections.add(section);
                rowCount++;
                if (rowCount % 100 == 0) {
                    System.out.println("Processed " + rowCount + " sections...");
                }
            }

            System.out.println("Total Sections created: " + rowCount);

        } catch (Exception e) {
            System.err.println("ERROR reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }

        return allSections;
    }

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
