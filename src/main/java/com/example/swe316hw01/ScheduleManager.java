package com.example.swe316hw01;

import java.io.File;
import java.util.List;

/**
 * Manages schedule-related operations:
 * - Loads Excel data
 * - Builds a StudentSchedule
 * - Fills computed fields (unique buildings, distance, route)
 */
public class ScheduleManager {

    private final ExcelScheduleLoader excelLoader = new ExcelScheduleLoader();
    private ScheduleService scheduleService;

    public boolean loadExcelData(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Excel file not found at: " + filePath);
                return false;
            }
            List<Section> data = excelLoader.load(filePath);
            SectionsHolder repo = new SectionsHolder(data);
            this.scheduleService = new ScheduleService(repo);
            System.out.println("Excel loaded successfully (" + data.size() + " sections).");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isReady() {
        return scheduleService != null;
    }

    /** Build schedule and enrich it with computed values. */
    public StudentSchedule generateResult(List<String> crns, String dayCode) {
        if (!isReady()) return null;

        StudentSchedule schedule = scheduleService.buildSchedule(crns, dayCode);

        int numBuildings = scheduleService.computeNumberOfDifferentBuildings(schedule);
        double distance  = scheduleService.computeTotalDistanceMeters(schedule);
        List<Building> routeBuildings = scheduleService.computeRouteBuildings(schedule);

        // fill computed fields
        schedule.setNumberOfBuildings(numBuildings);
        schedule.setTotalDistance(distance);
        schedule.setRouteBuildings(routeBuildings);

        return schedule;
    }
}
