package com.fabbroniko.ui.text;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AwtTextFactory implements TextFactory {

    private final FontProvider fontProvider;
    private final Graphics2D metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();

    public AwtTextFactory(final FontProvider fontProvider) {

        this.fontProvider = fontProvider;
    }


    @Override
    public BufferedImage createHeader(String text, final Color color) {
        return create(text, fontProvider.getHeaderFont(), color);
    }

    @Override
    public BufferedImage createLargeParagraph(String text, final Color color) {
        return create(text, fontProvider.getLargeParagraphFont(), color);
    }

    @Override
    public BufferedImage createParagraph(String text, final Color color) {
        return create(text, fontProvider.getParagraphFont(), color);
    }

    @Override
    public BufferedImage createSmallParagraph(String text, final Color color) {
        return create(text, fontProvider.getSmallParagraphFont(), color);
    }

    private BufferedImage create(final String text, final Font font, final Color color) {
        metrics.setFont(font);
        final int textWidth = metrics.getFontMetrics().stringWidth(text);
        final int textHeight = metrics.getFontMetrics().getHeight();

        final BufferedImage textImage = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D textGraphics = textImage.createGraphics();
        textGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        textGraphics.setColor(color);
        textGraphics.setFont(font);
        textGraphics.drawString(text, 0, font.getSize());
        return textImage;
    }
}
