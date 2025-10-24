package com.example.swe316hw01;

import java.io.File;
import java.util.List;

/**
 * Single responsibility: manage all schedule-related operations.
 * Handles Excel loading, building schedules, and computing results.
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

    public ScheduleResultData generateResult(List<String> crns, String dayCode) {
        if (!isReady()) return null;

        StudentSchedule schedule = scheduleService.buildSchedule(crns, dayCode);
        int numBuildings = scheduleService.computeNumberOfDifferentBuildings(schedule);
        double distance = scheduleService.computeTotalDistanceMeters(schedule);
        List<Building> routeBuildings = scheduleService.computeRouteBuildings(schedule);

        return new ScheduleResultData(schedule, numBuildings, distance, routeBuildings);
    }

    /** Small immutable DTO for results. */
    public static class ScheduleResultData {
        public final StudentSchedule schedule;
        public final int numberOfBuildings;
        public final double totalDistance;
        public final List<Building> routeBuildings;

        public ScheduleResultData(StudentSchedule schedule,
                                  int numberOfBuildings,
                                  double totalDistance,
                                  List<Building> routeBuildings) {
            this.schedule = schedule;
            this.numberOfBuildings = numberOfBuildings;
            this.totalDistance = totalDistance;
            this.routeBuildings = routeBuildings;
        }
    }
}
