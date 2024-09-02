package com.fabbroniko.resource;

import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.PreLoadedResource;
import com.fabbroniko.resource.dto.ResourceDto;
import org.example.annotation.Component;

@Component
public class AudioResourceLocator implements ResourceLocator {

    private final ResourceDto resourceDto;

    public AudioResourceLocator(final PreLoadedResource preLoadedResource) {
        this.resourceDto = preLoadedResource.getResourceDto();
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
