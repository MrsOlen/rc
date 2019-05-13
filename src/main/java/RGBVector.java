import java.io.Serializable;

public class RGBVector implements Serializable {

    private double red;
    private double green;
    private double blue;
    private double lightness;

    public RGBVector(int red, int green, int blue) {
        this.red = Math.pow(red / 255d, 2.2);
        this.green = Math.pow(green / 255d, 2.2);
        this.blue = Math.pow(blue / 255d, 2.2);
        lightness = 0.2126 * this.red + 0.7152 * this.green + 0.0722 * this.blue;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public double getLightness() {
        return lightness;
    }

    public void setLightness(double lightness) {
        this.lightness = lightness;
    }
}
