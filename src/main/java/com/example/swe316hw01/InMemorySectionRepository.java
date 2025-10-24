package com.example.swe316hw01;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Single responsibility: Keep sections in memory and provide query operations.
 * Immutable storage to prevent accidental external modification.
 */
public class InMemorySectionRepository {

    private final List<Section> data;

    public InMemorySectionRepository(List<Section> data) {
        this.data = List.copyOf(data == null ? List.of() : data);
    }

    /** Returns all sections. */
    public List<Section> findAll() {
        return data;
    }

    /**
     * Returns all rows that match any of the provided CRNs.
     * Does NOT stop at the first match — supports multiple rows per CRN (e.g., LEC/REC).
     */
    public List<Section> findByCrns(Collection<String> crns) {
        if (crns == null || crns.isEmpty()) return List.of();
        Set<String> wanted = crns.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Section> out = new ArrayList<>();
        for (Section s : data) {
            if (wanted.contains(s.getCrn())) {
                out.add(s);
            }
        }
        return out;
    }

    /**
     * Filters a given list by day code (U/M/T/W/R).
     * Relies on Meeting.isOnDay(...) to normalize/compare.
     */
    public static List<Section> filterByDay(List<Section> sections, String dayCode) {
        if (sections == null || sections.isEmpty()) return List.of();
        String normCode = dayCode == null ? "" : dayCode.trim().toUpperCase();
        if (normCode.isEmpty()) return List.of();

        List<Section> out = new ArrayList<>();
        for (Section s : sections) {
            Meeting m = (s != null) ? s.getMeeting() : null;
            if (m == null) continue;
            if (m.isOnDay(normCode)) {
                out.add(s);
            }
        }
        return out;
    }

    /** Convenience: query by CRNs then filter by day. */
    public List<Section> findByCrnsAndDay(Collection<String> crns, String dayCode) {
        return filterByDay(findByCrns(crns), dayCode);
    }
}
