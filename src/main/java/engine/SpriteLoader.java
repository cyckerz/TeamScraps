package engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class SpriteLoader {

    private SpriteLoader() { }

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite: " + path, e);
        }
    }
}

