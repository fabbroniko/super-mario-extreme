package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Resource;

import java.util.HashMap;
import java.util.Map;

public class CachedResourceIndexLoader implements ResourceIndexLoader {

    private final Map<String, Resource> cache = new HashMap<>();
    private final ResourceIndexLoader resourceIndexLoader;

    public CachedResourceIndexLoader(final ResourceIndexLoader resourceIndexLoader) {
        this.resourceIndexLoader = resourceIndexLoader;
    }

    @Override
    public Resource load(final String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, resourceIndexLoader.load(path));
        }

        return cache.get(path);
    }
}
