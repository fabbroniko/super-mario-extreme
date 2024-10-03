package com.fabbroniko.resource;

import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.ResourceDto;
import com.fabbroniko.sdi.annotation.Component;

@Component
public class AudioResourceLocator implements ResourceLocator {

    private final PreLoadedResource preLoadedResource;

    public AudioResourceLocator(final PreLoadedResource preLoadedResource) {
        this.preLoadedResource = preLoadedResource;
    }

    @Override
    public String findByName(final String name) {
        final ResourceDto resources = preLoadedResource.get();
        return resources.getClips()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new UndefinedResourceException(name, ResourceType.AUDIO, resources))
                .getPath();
    }
}
