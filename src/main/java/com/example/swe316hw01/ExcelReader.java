package com.example.swe316hw01;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    private List<Section> allSections;
    private Map<String, Building> buildingsMap; // Store buildings to avoid duplicates

    public ExcelReader() {
        allSections = new ArrayList<>();
        buildingsMap = new HashMap<>();
    }

    public void readExcelFile(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            int rowCount = 0;

            System.out.println("===============================================");
            System.out.println("Reading Excel Data and Creating Section Objects...");
            System.out.println("===============================================");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String crn = getValue(row, 1);
                String courseCode = getValue(row, 2);
                String deptName = getValue(row, 3);
                String sectionNumber = getValue(row, 4);
                String courseTitle = getValue(row, 5);
                String days = getValue(row, 7);
                String startTime = getValue(row, 8);
                String endTime = getValue(row, 9);
                String buildingNumber = getValue(row, 10);
                String roomNumber = getValue(row, 11);
                String instructor = getValue(row, 12);

                if (crn.isEmpty()) continue;

                Section section = createSection(
                        crn, courseCode, deptName, sectionNumber, courseTitle,
                        days, startTime, endTime, buildingNumber, roomNumber, instructor
                );

                allSections.add(section);
                rowCount++;

                if (rowCount % 100 == 0) {
                    System.out.println("Processed " + rowCount + " sections...");
                }
            }

            System.out.println("===============================================");
            System.out.println("Total Sections Created: " + rowCount);
            System.out.println("===============================================");

            workbook.close();
            file.close();

        } catch (Exception e) {
            System.err.println("ERROR reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Section createSection(String crn, String courseCode, String deptName,
                                  String sectionNumber, String courseTitle,
                                  String days, String startTime, String endTime,
                                  String buildingNumber, String roomNumber, String instructor) {

        Building building = null;
        if (!buildingNumber.isEmpty()) {
            // Get or create building
            if (!buildingsMap.containsKey(buildingNumber)) {
                building = new Building(buildingNumber); // Dummy coordinates for now
                buildingsMap.put(buildingNumber, building);
            } else {
                building = buildingsMap.get(buildingNumber);
            }
        }

        Room room = null;
        if (!roomNumber.isEmpty() && building != null) {
            room = new Room(roomNumber, building);
        }

        Meeting meeting = new Meeting(
                days.isEmpty() ? "N/A" : days,
                startTime.isEmpty() ? "N/A" : startTime,
                endTime.isEmpty() ? "N/A" : endTime,
                room,
                building
        );

        // Create Section following correct constructor order:
        // (departmentName, courseTitle, courseName, crn, sectionNumber, type, meeting, instructor)
        Section section = new Section(
                deptName.isEmpty() ? "Unknown" : deptName,        // departmentName
                courseCode.isEmpty() ? "N/A" : courseCode,        // courseTitle (e.g., "SWE 316")
                courseTitle.isEmpty() ? "N/A" : courseTitle,      // courseName (e.g., "Software Design...")
                crn,                                               // crn
                sectionNumber.isEmpty() ? "N/A" : sectionNumber,  // sectionNumber
                "LEC",                                            // type
                meeting,                                          // meeting
                instructor.isEmpty() ? "TBA" : instructor         // instructor
        );

        return section;
    }

    public List<Section> getAllSections() {
        return allSections;
    }

    public List<Section> getSectionsByCRNs(String[] crns) {
        List<Section> filtered = new ArrayList<>();

        for (String crn : crns) {
            crn = crn.trim();
            for (Section section : allSections) {
                if (section.getCrn().equals(crn)) {
                    filtered.add(section);
                    break;
                }
            }
        }

        return filtered;
    }

    public List<Section> filterSectionsByDay(List<Section> sections, String day) {
        List<Section> filtered = new ArrayList<>();

        for (Section section : sections) {
            if (section.meetsOnDay(day)) {
                filtered.add(section);
            }
        }

        return filtered;
    }

    private String getValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        String value = "";
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                value = String.valueOf((int) cell.getNumericCellValue());
                break;
            default:
                value = "";
        }

        return value.trim();
    }
}