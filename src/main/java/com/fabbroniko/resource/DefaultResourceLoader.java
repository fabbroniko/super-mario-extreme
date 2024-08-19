package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;

import java.io.InputStream;

public class DefaultResourceLoader implements ResourceLoader {

    @Override
    public InputStream load(final String path) {
        final InputStream audioClipStream = getClass().getResourceAsStream(path);
        if(audioClipStream == null) {
            throw new ResourceNotFoundException(path);
        }

        return audioClipStream;
    }
}
