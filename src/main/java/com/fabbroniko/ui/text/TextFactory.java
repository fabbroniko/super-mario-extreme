package com.fabbroniko.ui.text;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface TextFactory {

    BufferedImage createHeader(final String text, final Color color);

    BufferedImage createLargeParagraph(final String text, final Color color);

    BufferedImage createParagraph(final String text, final Color color);

    BufferedImage createSmallParagraph(final String text, final Color color);
}
