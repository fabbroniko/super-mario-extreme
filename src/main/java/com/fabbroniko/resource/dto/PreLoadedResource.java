package com.fabbroniko.resource.dto;

import com.fabbroniko.resource.ResourceIndexLoader;
import com.fabbroniko.sdi.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class PreLoadedResource {

    private static final String RESOURCE_INDEX_LOCATION = "/resources.index";

    private final ResourceDto resourceDto;

    public PreLoadedResource(final ResourceIndexLoader resourceIndexLoader) {

        this.resourceDto = resourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
    }
}
