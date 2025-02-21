package blog.javamastery.library.ui.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class FontManager {
    private final static int MIN_SIZE = 10;
    private final static int MAX_SIZE = 24;

    private final Font systemFont;
    private final int defaultSize;
    private int currentSize;
    private final Runnable updateCallback;

    public FontManager(int defaultSize, Runnable updateCallback) {
        this.defaultSize = defaultSize;
        this.currentSize = defaultSize;
        this.updateCallback = updateCallback;
        this.systemFont = getSystemFont();
        setGlobalFont(systemFont, currentSize);
    }

    public int getSize() {
        return currentSize;
    }

    public void adjustSize(int sizeChange) {
        int newSize = currentSize + sizeChange;
        if (newSize >= MIN_SIZE && newSize <= MAX_SIZE) {
            setGlobalFont(systemFont, newSize);
            updateCallback.run();
        }
    }

    public void resetSize() {
        setGlobalFont(systemFont, defaultSize);
        updateCallback.run();
    }

    private void setGlobalFont(Font font, int size) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                Font newFont = font.deriveFont((float) size);
                UIManager.put(key, new FontUIResource(newFont));
            }
        }
        currentSize = size;
    }

    private Font getSystemFont() {
        String os = System.getProperty("os.name").toLowerCase();
        String fontName = switch (os) {
            case String s when s.contains("mac") -> ".AppleSystemUIFont";
            case String s when s.contains("windows") -> "Segoe UI";
            default -> "Dialog";
        };
        return new Font(fontName, Font.PLAIN, MIN_SIZE + 2);
    }
}
