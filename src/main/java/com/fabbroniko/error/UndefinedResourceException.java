package com.fabbroniko.error;

import com.fabbroniko.resource.ResourceType;

public class UndefinedResourceException extends RuntimeException {

    public UndefinedResourceException(final String name, final ResourceType resourceType) {
        super("The %s resource '%s' was not defined in the resource index file.".formatted(resourceType, name));
    }
}
