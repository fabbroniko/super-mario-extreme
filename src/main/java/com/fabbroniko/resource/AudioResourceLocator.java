package com.fabbroniko.resource;

import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.ResourceDto;

public class AudioResourceLocator implements ResourceLocator {

    private final ResourceDto resourceDto;

    public AudioResourceLocator(final ResourceDto resourceDto) {
        this.resourceDto = resourceDto;
    }

    @Override
    public String findByName(final String name) {
        return resourceDto.getClips()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new UndefinedResourceException(name, ResourceType.AUDIO))
                .getPath();
    }
}
