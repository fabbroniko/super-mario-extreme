package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.error.UndefinedResourceException;
import com.fabbroniko.resource.dto.BackgroundDto;
import com.fabbroniko.resource.dto.ResourceDto;
import com.fabbroniko.sdi.annotation.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Component
public class DiskImageLoader implements ImageLoader {

    private final PreLoadedResource preLoadedResource;
    private final ResourceLoader resourceLoader;

    public DiskImageLoader(final PreLoadedResource preLoadedResource,
                           final ResourceLoader resourceLoader) {
        this.preLoadedResource = preLoadedResource;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BufferedImage findBackgroundByName(final String name) {
        final ResourceDto resources = preLoadedResource.get();
        final BackgroundDto background = resources.getBackgrounds()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new UndefinedResourceException(name, ResourceType.BACKGROUND, resources));

        return loadImageFromDisk(background.getPath());
    }

    @Override
    public BufferedImage findTileMap() {
        return loadImageFromDisk(preLoadedResource.get().getTileMap().getPath());
    }

    @Override
    public BufferedImage findSpritesByName(String name) {
        return loadImageFromDisk(name);
    }

    private BufferedImage loadImageFromDisk(final String path) {
        try (final InputStream inputStream = resourceLoader.load(path)) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            throw new ResourceNotFoundException(path);
        }
    }
}
