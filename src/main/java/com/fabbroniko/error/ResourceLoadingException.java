package com.fabbroniko.error;

public class ResourceLoadingException extends RuntimeException {

    public ResourceLoadingException(final Throwable throwable) {
        super(throwable);
    }
}
