package service;

import java.util.HashMap;
import java.util.Map;

public class MetricsService {
    private Map<String, Integer> searchCounters = new HashMap<>();
    private Map<String, Long> searchDurations = new HashMap<>();
    private Map<String, Integer> searchResultCounts = new HashMap<>();

    public void recordSearch(String searchType, long duration, int resultCount) {
        searchCounters.put(searchType, searchCounters.getOrDefault(searchType, 0) + 1);
        searchDurations.put(searchType, duration);
        searchResultCounts.put(searchType, resultCount);
    }

    public void showMetrics() {
        System.out.println("\n=== МЕТРИКИ СИСТЕМЫ ===");
        System.out.println("📊 Статистика поиска:");

        for (String searchType : searchCounters.keySet()) {
            int count = searchCounters.get(searchType);
            Long duration = searchDurations.get(searchType);
            Integer resultCount = searchResultCounts.get(searchType);

            System.out.printf("   %s: %d запросов, время: %d мс, результаты: %d%n",
                    searchType, count, duration != null ? duration : 0,
                    resultCount != null ? resultCount : 0);
        }

        System.out.println("=== КОНЕЦ МЕТРИК ===");
    }

    public int getTotalSearchCount() {
        return searchCounters.values().stream().mapToInt(Integer::intValue).sum();
    }
}