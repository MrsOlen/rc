import java.io.Serializable;
import java.util.List;

public class XMPParams implements Serializable {

    private int temperature;
    private int tint;
    private double exposure;
    private int contrast;
    private int highlights;
    private int shadows;
    private int whites;
    private int blacks;
    private int clarity;
    private int dehaze;
    private int vibrance;
    private int saturation;

    public XMPParams() {}

    public XMPParams(XMPParams xmpParams1, double k1, XMPParams xmpParams2, double k2) {
        temperature = (int)(k1 * xmpParams1.temperature + k2 * xmpParams2.temperature);
        tint = (int)(k1 * xmpParams1.tint + k2 * xmpParams2.tint);
        exposure = k1 * xmpParams1.exposure + k2 * xmpParams2.exposure;
        contrast = (int)(k1 * xmpParams1.contrast + k2 * xmpParams2.contrast);
        highlights = (int)(k1 * xmpParams1.highlights + k2 * xmpParams2.highlights);
        shadows = (int)(k1 * xmpParams1.shadows + k2 * xmpParams2.shadows);
        whites = (int)(k1 * xmpParams1.whites + k2 * xmpParams2.whites);
        blacks = (int)(k1 * xmpParams1.blacks + k2 * xmpParams2.blacks);
        clarity = (int)(k1 * xmpParams1.clarity + k2 * xmpParams2.clarity);
        dehaze = (int)(k1 * xmpParams1.dehaze + k2 * xmpParams2.dehaze);
        vibrance = (int)(k1 * xmpParams1.vibrance + k2 * xmpParams2.vibrance);
        saturation = (int)(k1 * xmpParams1.saturation + k2 * xmpParams2.saturation);
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTint() {
        return tint;
    }

    public void setTint(int tint) {
        this.tint = tint;
    }

    public double getExposure() {
        return exposure;
    }

    public void setExposure(double exposure) {
        this.exposure = exposure;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getHighlights() {
        return highlights;
    }

    public void setHighlights(int highlights) {
        this.highlights = highlights;
    }

    public int getShadows() {
        return shadows;
    }

    public void setShadows(int shadows) {
        this.shadows = shadows;
    }

    public int getWhites() {
        return whites;
    }

    public void setWhites(int whites) {
        this.whites = whites;
    }

    public int getBlacks() {
        return blacks;
    }

    public void setBlacks(int blacks) {
        this.blacks = blacks;
    }

    public int getClarity() {
        return clarity;
    }

    public void setClarity(int clarity) {
        this.clarity = clarity;
    }

    public int getDehaze() {
        return dehaze;
    }

    public void setDehaze(int dehaze) {
        this.dehaze = dehaze;
    }

    public int getVibrance() {
        return vibrance;
    }

    public void setVibrance(int vibrance) {
        this.vibrance = vibrance;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
