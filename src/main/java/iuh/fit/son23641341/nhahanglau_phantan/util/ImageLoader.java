package iuh.fit.son23641341.nhahanglau_phantan.util;

import javax.swing.*;

/**
 * Utility class to safely load images from classpath resources.
 * Handles null resources gracefully.
 */
public class ImageLoader {

    /**
     * Load ImageIcon from resource path. Returns null if resource not found.
     * @param resourcePath the resource path starting with "/" e.g., "/imgs/logo.png"
     * @return ImageIcon or null if not found
     */
    public static ImageIcon loadImageIcon(String resourcePath) {
        try {
            java.net.URL url = ImageLoader.class.getResource(resourcePath);
            if (url != null) {
                return new ImageIcon(url);
            } else {
                System.err.println("WARNING: Image resource not found: " + resourcePath);
                return null;
            }
        } catch (Exception e) {
            System.err.println("ERROR loading image: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load Image from resource path. Returns null if resource not found.
     * @param resourcePath the resource path starting with "/" e.g., "/imgs/logo.png"
     * @return Image or null if not found
     */
    public static java.awt.Image loadImage(String resourcePath) {
        ImageIcon icon = loadImageIcon(resourcePath);
        return (icon != null) ? icon.getImage() : null;
    }
}

