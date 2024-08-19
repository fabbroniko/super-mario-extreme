package com.fabbroniko.resource;

import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.Resource;

public class AudioResourceLocator implements ResourceLocator {

    private final Resource resource;

    public AudioResourceLocator(final Resource resource) {
        this.resource = resource;
    }

    @Override
    public String findByName(final String name) {
        return resource.getClips()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new UndefinedResourceException(name, ResourceType.AUDIO))
                .getPath();
    }
}
