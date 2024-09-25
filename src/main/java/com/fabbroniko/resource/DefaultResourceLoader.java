package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.sdi.annotation.Component;

import java.io.InputStream;

@Component
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
