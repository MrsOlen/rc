import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class Metric {

    private static final int RGB_MATRIX_SIZE = 10;

    private RGBVector[][] rgbMatrix;

    public Metric(File imageFile) {
        BufferedImage image = getImage(imageFile);
        rgbMatrix = new RGBVector[RGB_MATRIX_SIZE][RGB_MATRIX_SIZE];
        int width = image.getWidth() / RGB_MATRIX_SIZE;
        int height = image.getHeight() / RGB_MATRIX_SIZE;

        for (int i = 0; i < RGB_MATRIX_SIZE; i++) {
            for (int j = 0; j < RGB_MATRIX_SIZE; j++) {
                int red = 0, green = 0, blue = 0;
                for (int x = i * width; x < (i + 1) * width - 1; x++) {
                    for (int y = j * height; y < (j + 1) * height; y++) {
                        Color color = new Color(image.getRGB(x, y));
                        red += color.getRed();
                        green += color.getGreen();
                        blue += color.getBlue();
                    }
                }
                int pixelsCount = (width - 1) * (height - 1);
                rgbMatrix[i][j] = new RGBVector(red/pixelsCount, green/pixelsCount, blue/pixelsCount);
            }
        }
    }

    public List<Double> getNormalizedVector() {
        List<Double> vector = new Vector<>();
        for (int i = 0; i < RGB_MATRIX_SIZE; i++) {
            for (int j = 0; j < RGB_MATRIX_SIZE; j++) {
                vector.add(rgbMatrix[i][j].getRed());
                vector.add(rgbMatrix[i][j].getGreen());
                vector.add(rgbMatrix[i][j].getBlue());
                vector.add(rgbMatrix[i][j].getLightness());
            }
        }
        return vector;
    }

    private BufferedImage getImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось получить картинку из файла " + file.getAbsolutePath());
        }
    }
}
