package com.fabbroniko.scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OptionFactoryImpl implements OptionFactory {

    private final TextFactory textFactory;

    public OptionFactoryImpl(final TextFactory textFactory) {
        this.textFactory = textFactory;
    }

    @Override
    public BufferedImage getMainMenuOption(final String text, final Color color) {
        final BufferedImage option = new BufferedImage(360, 120, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = option.createGraphics();

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(12));
        graphics.drawRect(0, 0, 360, 120);

        final BufferedImage textImage = textFactory.createLargeParagraph(text, color);
        final int x = (360 - textImage.getWidth()) / 2;
        graphics.drawImage(textImage, null, x, 10);
        return option;
    }
}
