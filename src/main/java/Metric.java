import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Metric implements Serializable {

    private static final int RGB_MATRIX_SIZE = 3;

    private RGBVector[][] rgbMatrix;

    public Metric(BufferedImage image) {
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

    public static double getDist(Metric metric1, Metric metric2) {
        double sum = 0;
        for (int i = 0; i < RGB_MATRIX_SIZE; i++) {
            for (int j = 0; j < RGB_MATRIX_SIZE; j++) {
                sum+= Math.pow(metric1.getRgbMatrix()[i][j].getRed() - metric2.getRgbMatrix()[i][j].getRed(), 2) +
                        Math.pow(metric1.getRgbMatrix()[i][j].getGreen() - metric2.getRgbMatrix()[i][j].getGreen(), 2) +
                        Math.pow(metric1.getRgbMatrix()[i][j].getBlue() - metric2.getRgbMatrix()[i][j].getBlue(), 2) +
                        Math.pow(metric1.getRgbMatrix()[i][j].getLightness() - metric2.getRgbMatrix()[i][j].getLightness(), 2);
            }
        }
        return Math.sqrt(sum);
    }

    public RGBVector[][] getRgbMatrix() {
        return rgbMatrix;
    }

    public void setRgbMatrix(RGBVector[][] rgbMatrix) {
        this.rgbMatrix = rgbMatrix;
    }
}
