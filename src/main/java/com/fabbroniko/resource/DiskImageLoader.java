package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.resource.dto.BackgroundDto;
import com.fabbroniko.resource.dto.PreLoadedResource;
import com.fabbroniko.resource.dto.ResourceDto;
import com.fabbroniko.sdi.annotation.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Component
public class DiskImageLoader implements ImageLoader {

    private final ResourceDto resourceDto;

    public DiskImageLoader(final PreLoadedResource preLoadedResource) {
        this.resourceDto = preLoadedResource.getResourceDto();
    }


    @Override
    public BufferedImage findBackgroundByName(String name) {
        final BackgroundDto background = resourceDto.getBackgrounds()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(name));

        return loadImageFromDisk(background.getPath());
    }

    @Override
    public BufferedImage findTileMap() {
        return loadImageFromDisk(resourceDto.getTileMap().getPath());
    }

    @Override
    public BufferedImage findSpritesByName(String name) {
        return loadImageFromDisk(name);
    }

    private BufferedImage loadImageFromDisk(final String path) {
        try (final InputStream imageInputStream = getClass().getResourceAsStream(path)) {
            return ImageIO.read(imageInputStream);
        } catch (Exception e) {
            throw new ResourceNotFoundException(path);
        }
    }
}
