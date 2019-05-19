import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Metric {

    private static final int RGB_MATRIX_SIZE = 3;

    private RGBVector[][] rgbMatrix;
    private double contrast;
    private double contrast2;
    private double saturation;

    public Metric(File imageFile) {
        BufferedImage image = getImage(imageFile);
        rgbMatrix = new RGBVector[RGB_MATRIX_SIZE][RGB_MATRIX_SIZE];
        int width = image.getWidth() / RGB_MATRIX_SIZE;
        int height = image.getHeight() / RGB_MATRIX_SIZE;

        int contrastPixelCount = image.getWidth() * image.getHeight() / 100;
        List<Double> lightnesses = new ArrayList<>();
        double lightnessSum = 0, saturationSum = 0;

        for (int i = 0; i < RGB_MATRIX_SIZE; i++) {
            for (int j = 0; j < RGB_MATRIX_SIZE; j++) {
                double redSum = 0, greenSum = 0, blueSum = 0;
                for (int x = i * width; x < (i + 1) * width - 1; x++) {
                    for (int y = j * height; y < (j + 1) * height; y++) {
                        Color color = new Color(image.getRGB(x, y));
                        double red = Math.pow(color.getRed() / 255d, 2.2);
                        double green = Math.pow(color.getGreen() / 255d, 2.2);
                        double blue = Math.pow(color.getBlue() / 255d, 2.2);
                        redSum += red;
                        greenSum += green;
                        blueSum += blue;
                        double lightness = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
                        double max = getMax(color.getRed(), color.getGreen(), color.getBlue());
                        double saturation = max != 0 ? (1 - getMin(color.getRed(), color.getGreen(), color.getBlue()) / max) : 0;
                        if (lightness > 1) {
                            System.out.println("1");
                        }
                        lightnesses.add(lightness);
                        lightnessSum += lightness;
                        saturationSum += saturation;
                    }
                }
                int pixelsCount = (width - 1) * (height - 1);
                rgbMatrix[i][j] = new RGBVector(redSum / pixelsCount, greenSum / pixelsCount, blueSum / pixelsCount);
            }
        }
        double avgLightness = lightnessSum / (image.getWidth() * image.getHeight());
        lightnesses.sort(Double::compareTo);
        double lightPoints = lightnesses.subList(lightnesses.size() - contrastPixelCount, lightnesses.size()).stream().mapToDouble(l -> l).sum();
        double blackPoints = lightnesses.subList(0, contrastPixelCount).stream().mapToDouble(l -> l).sum();
        contrast = blackPoints / lightPoints;
        contrast2 = lightnesses.stream().mapToDouble(l -> Math.pow(l - avgLightness, 2)).sum() / (image.getWidth() * image.getHeight());
        saturation = saturationSum / (image.getWidth() * image.getHeight());
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
        vector.add(contrast);
        vector.add(contrast2);
        vector.add(saturation);
        return vector;
    }

    public double getMax(double a, double b, double c) {
        if (a > b && a > c) {
            return a;
        }
        if (b > a && b > c) {
            return b;
        }
        return c;
    }

    public double getMin(double a, double b, double c) {
        if (a < b && a < c) {
            return a;
        }
        if (b < a && b < c) {
            return b;
        }
        return c;
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
