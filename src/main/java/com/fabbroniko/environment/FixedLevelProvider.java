package com.fabbroniko.environment;

import com.fabbroniko.resource.dto.LevelDto;
import com.fabbroniko.sdi.annotation.Component;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

@Component
public class FixedLevelProvider implements LevelProvider {

    @Override
    @SneakyThrows
    public LevelDto getLevel() {
        return new XmlMapper().readValue(getClass().getResource("/levels/testing.xml"), LevelDto.class);
    }
}
