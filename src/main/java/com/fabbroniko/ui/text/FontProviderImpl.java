package com.fabbroniko.ui.text;

import javax.swing.*;
import java.awt.Font;

public class FontProviderImpl implements FontProvider {

    protected static final Font HEADER_FONT = new JPanel().getFont().deriveFont(Font.BOLD, 80);
    protected static final Font LARGE_PARAGRAPH_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 80);
    protected static final Font PARAGRAPH_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 48);
    protected static final Font SMALL_PARAGRAPH_FONT = new JPanel().getFont().deriveFont(Font.PLAIN, 40);

    @Override
    public Font getHeaderFont() {
        return HEADER_FONT;
    }

    @Override
    public Font getLargeParagraphFont() {
        return LARGE_PARAGRAPH_FONT;
    }

    @Override
    public Font getParagraphFont() {
        return PARAGRAPH_FONT;
    }

    @Override
    public Font getSmallParagraphFont() {
        return SMALL_PARAGRAPH_FONT;
    }
}
