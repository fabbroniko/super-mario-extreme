package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.ResourceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.example.annotation.Component;
import org.example.annotation.Qualifier;

import java.util.function.Supplier;

@Log4j2
@Component
public class DiskResourceIndexLoader implements ResourceIndexLoader {

    private final ObjectMapper objectMapper;
    private final PathToUrlConverter pathToUrlConverter;

    public DiskResourceIndexLoader(@Qualifier("xmlMapperSupplier") final Supplier<ObjectMapper> xmlObjectMapper,
                                   final PathToUrlConverter pathToUrlConverter) {

        this.objectMapper = xmlObjectMapper.get();
        this.pathToUrlConverter = pathToUrlConverter;
    }

    @SneakyThrows
    public ResourceDto load(final String path) {
        return objectMapper.readValue(pathToUrlConverter.locate(path), ResourceDto.class);
    }
}
