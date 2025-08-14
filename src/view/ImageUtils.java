package view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Utility class for handling book cover and profile images.
 */
public class ImageUtils {

    private static final String COVERS_DIR = "book_covers";
    private static final int COVER_WIDTH = 190;
    private static final int COVER_HEIGHT = 220;

    public static void saveBookImage(String isbn, String sourcePath) {
        try {
            File destDir = new File(COVERS_DIR);
            if (!destDir.exists()) destDir.mkdir();

            BufferedImage originalImage = ImageIO.read(new File(sourcePath));
            Image resizedImage = originalImage.getScaledInstance(COVER_WIDTH, COVER_HEIGHT, Image.SCALE_SMOOTH);
            
            BufferedImage outputImage = new BufferedImage(COVER_WIDTH, COVER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(resizedImage, 0, 0, null);
            g2d.dispose();

            File outputFile = new File(destDir, isbn + ".png");
            ImageIO.write(outputImage, "png", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving image for ISBN " + isbn + ": " + e.getMessage());
        }
    }

    public static ImageIcon loadBookImage(String isbn) {
        File imageFile = new File(COVERS_DIR, isbn + ".png");
        if (imageFile.exists()) {
            return new ImageIcon(new ImageIcon(imageFile.getAbsolutePath()).getImage().getScaledInstance(COVER_WIDTH, COVER_HEIGHT, Image.SCALE_SMOOTH));
        }
        return null;
    }

    public static void saveProfilePicture(String username, String sourcePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(sourcePath));
            File outputFile = new File("profile_" + username + ".png");
            ImageIO.write(originalImage, "png", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving profile picture for " + username + ": " + e.getMessage());
        }
    }

    public static ImageIcon loadProfilePicture(String username, int size) {
        try {
            File profilePicFile = new File("profile_" + username + ".png");
            BufferedImage master;
            if (profilePicFile.exists()) {
                master = ImageIO.read(profilePicFile);
            } else {
                // Create a default placeholder image if none exists
                master = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = master.createGraphics();
                g2d.setColor(UIFactory.COLOR_ACCENT);
                g2d.fillRect(0, 0, size, size);
                g2d.setColor(Color.WHITE);
                g2d.setFont(UIFactory.FONT_TITLE.deriveFont((float)size / 2));
                String initial = username.substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (size - fm.stringWidth(initial)) / 2;
                int y = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(initial, x, y);
                g2d.dispose();
            }

            // Create a circular version of the image
            int diameter = Math.min(master.getWidth(), master.getHeight());
            BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = mask.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fill(new Ellipse2D.Double(0, 0, diameter, diameter));
            g2d.dispose();

            BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            g2d = masked.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = (diameter - master.getWidth()) / 2;
            int y = (diameter - master.getHeight()) / 2;
            g2d.drawImage(master, x, y, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
            g2d.drawImage(mask, 0, 0, null);
            g2d.dispose();

            return new ImageIcon(masked.getScaledInstance(size, size, Image.SCALE_SMOOTH));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
