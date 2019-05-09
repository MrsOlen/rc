import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Store {

    private Map<Metric, XMPParams> store;

    public Store() {
        store = new HashMap<>();
    }

    public void add(File imageFile, File xmpFile) {
        store.put(new Metric(getImage(imageFile)), XMPParser.parse(xmpFile));
    }

    public XMPParams get(File imageFile) {
        return store.get(getNearMetric(getImage(imageFile)));
    }

    private Metric getNearMetric(BufferedImage bufferedImage) {
        Metric currentMetric = new Metric(bufferedImage);
        int minDist = Integer.MAX_VALUE;
        Metric result = null;
        for (Metric metric: store.keySet()) {
            int dist = Metric.getDist(currentMetric, metric);
            if (dist < minDist) {
                minDist = dist;
                result = metric;
            }
        }
        return result;
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
