package com.fabbroniko.resource.dto;

import com.fabbroniko.resource.ResourceIndexLoader;
import lombok.Getter;

@Getter
public class PreLoadedResource {

    private static final String RESOURCE_INDEX_LOCATION = "/resources.index";

    private final ResourceDto resourceDto;

    public PreLoadedResource(final ResourceIndexLoader resourceIndexLoader) {

        this.resourceDto = resourceIndexLoader.load(RESOURCE_INDEX_LOCATION);
    }
}
