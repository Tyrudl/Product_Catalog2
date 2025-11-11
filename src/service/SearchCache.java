package service;

import model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCache {
    private Map<String, List<Product>> cache = new HashMap<>();
    private Map<String, Long> cacheTimestamps = new HashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 минут

    public void put(String cacheKey, List<Product> products) {
        cache.put(cacheKey, new ArrayList<>(products));
        cacheTimestamps.put(cacheKey, System.currentTimeMillis());
    }

    public List<Product> get(String cacheKey) {
        if (!cache.containsKey(cacheKey)) {
            return null;
        }

        long timestamp = cacheTimestamps.get(cacheKey);
        if (System.currentTimeMillis() - timestamp > CACHE_DURATION) {
            cache.remove(cacheKey);
            cacheTimestamps.remove(cacheKey);
            return null;
        }

        return new ArrayList<>(cache.get(cacheKey));
    }

    public void invalidate(String cacheKey) {
        cache.remove(cacheKey);
        cacheTimestamps.remove(cacheKey);
    }

    public void clear() {
        cache.clear();
        cacheTimestamps.clear();
    }

    public int getSize() {
        return cache.size();
    }
}