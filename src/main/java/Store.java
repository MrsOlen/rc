import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Store {

    private Map<Metric, XMPParams> store;

    public Store() {
        store = new HashMap<>();
    }

    public void add(File imageFile, File xmpFile) {
        store.put(new Metric(getImage(imageFile)), XMPParser.parse(xmpFile));
    }

    public XMPParams get(File imageFile) {
        List<NearMetric> nearMetrics = getNearMetrics(getImage(imageFile));
        double distSum = nearMetrics.stream().mapToDouble(NearMetric::getDistance).sum();
        return store.get(getNearMetrics(getImage(imageFile)));
    }

    private List<NearMetric> getNearMetrics(BufferedImage bufferedImage) {
        Metric currentMetric = new Metric(bufferedImage);
        List<NearMetric> nearMetrics = new ArrayList<>();
        for (Metric metric: store.keySet()) {
            double dist = Metric.getDist(currentMetric, metric);
            nearMetrics.add(new NearMetric(metric, dist));
        }
        nearMetrics.sort(Comparator.comparing(NearMetric::getDistance));
        return nearMetrics.subList(0, 1);
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
