package com.fabbroniko.error;

import com.fabbroniko.resource.ResourceType;
import com.fabbroniko.resource.dto.ResourceDto;

public class UndefinedResourceException extends RuntimeException {

    private static final String BASE_ERROR_MESSAGE =
        """
        Resource '%s' of type '%s' was not defined in the resource index file.
        """;

    private static final String ERROR_MESSAGE = """
        Resource '%s' of type '%s' was not defined in the resource index file.
        The following resources are defined:
        
        %s
        """;
    public UndefinedResourceException(final String name, final ResourceType resourceType, final ResourceDto resources) {
        super(ERROR_MESSAGE.formatted(name, resourceType, resources));
    }

    public UndefinedResourceException(final String name, final ResourceType resourceType) {
        super(BASE_ERROR_MESSAGE.formatted(name, resourceType));
    }
}
