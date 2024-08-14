package com.fabbroniko.resource;

import com.fabbroniko.error.ResourceNotFoundException;
import com.fabbroniko.resource.dto.Background;
import com.fabbroniko.resource.dto.Resource;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Log4j2
public class DiskImageLoader implements ImageLoader {

    private final Resource resource;

    public DiskImageLoader(final Resource resource) {
        this.resource = resource;
    }


    @Override
    public BufferedImage findBackgroundByName(String name) {
        log.trace("Loading background image {} from disk.", name);
        final Background background = resource.getBackgrounds()
                .stream()
                .filter(c -> c.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(name));

        return loadImageFromDisk(background.getPath());
    }

    @Override
    public BufferedImage findTileMap() {
        log.trace("Loading tile map set (uncut).");

        return loadImageFromDisk(resource.getTilemap().getPath());
    }

    @Override
    public BufferedImage findSpritesByName(String name) {
        return loadImageFromDisk(name);
    }

    private BufferedImage loadImageFromDisk(final String path) {
        log.trace("Loading image with path {} from disk.", path);

        try (final InputStream imageInputStream = getClass().getResourceAsStream(path)) {
            return ImageIO.read(imageInputStream);
        } catch (Exception e) {
            log.error("Unable to load image {} from disk. Exception {}", path, e.getMessage());
            throw new ResourceNotFoundException(path);
        }
    }
}
