package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DiskResourceIndexLoader implements ResourceIndexLoader {

    private final ObjectMapper mapper;
    private final PathToUrlConverter pathToUrlConverter;

    public DiskResourceIndexLoader(final ObjectMapper mapper,
                               final PathToUrlConverter pathToUrlConverter) {

        this.mapper = mapper;
        this.pathToUrlConverter = pathToUrlConverter;
    }

    @SneakyThrows
    public Resource load(final String path) {
        return mapper.readValue(pathToUrlConverter.locate(path), Resource.class);
    }
}
