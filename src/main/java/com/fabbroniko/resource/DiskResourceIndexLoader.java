package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DiskResourceIndexLoader implements ResourceIndexLoader {

    private final ObjectMapper mapper;
    private final ResourceLocator resourceLocator;

    public DiskResourceIndexLoader(final ObjectMapper mapper,
                               final ResourceLocator resourceLocator) {

        this.mapper = mapper;
        this.resourceLocator = resourceLocator;
    }

    @SneakyThrows
    public Resource load(final String path) {
        return mapper.readValue(resourceLocator.locate(path), Resource.class);
    }
}
