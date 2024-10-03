package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.ResourceDto;
import com.fabbroniko.sdi.annotation.Component;

@Component
public class PreLoadedResource implements ResourceRetriever<ResourceDto> {

    private static final String RESOURCE_INDEX_LOCATION = "/resources.index";

    private final ResourceIndexLoader resourceIndexLoader;
    private volatile ResourceDto resourceDto;

    public PreLoadedResource(final ResourceIndexLoader resourceIndexLoader) {
        this.resourceIndexLoader = resourceIndexLoader;
    }

    @Override
    public synchronized ResourceDto get() {
        if(resourceDto == null) {
            resourceDto = resourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
        }

        return resourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
    }
}
