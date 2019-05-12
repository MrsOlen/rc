import java.util.ArrayList;
import java.util.List;

public class XMPParams {

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

    public XMPParams(List<Double> normalizedVector) {
        temperature = (int)(normalizedVector.get(0) * 48000 + 2000);
        tint = (int)(normalizedVector.get(1) * 300 - 150);
        exposure = normalizedVector.get(2) * 10d - 5d;
        contrast = (int)(normalizedVector.get(3) * 200 - 100);
        highlights = (int)(normalizedVector.get(4) * 200 - 100);
        shadows = (int)(normalizedVector.get(5) * 200 - 100);
        whites = (int)(normalizedVector.get(6) * 200 - 100);
        blacks = (int)(normalizedVector.get(7) * 200 - 100);
        clarity = (int)(normalizedVector.get(8) * 200 - 100);
        dehaze = (int)(normalizedVector.get(9) * 200 - 100);
        vibrance = (int)(normalizedVector.get(10) * 200 - 100);
        saturation = (int)(normalizedVector.get(11) * 200 - 100);
    }

    public List<Double> getNormalizedVector() {
        List<Double> vector = new ArrayList<>();
        vector.add((temperature - 2000d) / 48000d);
        vector.add((tint + 150d) / 300d);
        vector.add((exposure + 5d) / 10d);
        vector.add((contrast + 100d) / 200d);
        vector.add((highlights + 100d) / 200d);
        vector.add((shadows + 100d) / 200d);
        vector.add((whites + 100d) / 200d);
        vector.add((blacks + 100d) / 200d);
        vector.add((clarity + 100d) / 200d);
        vector.add((dehaze + 100d) / 200d);
        vector.add((vibrance + 100d) / 200d);
        vector.add((saturation + 100d) / 200d);
        return vector;
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
