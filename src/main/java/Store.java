import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Store implements Serializable {

    private Map<Metric, XMPParams> store;

    public Store() {
        store = new HashMap<>();
    }

    public void add(File imageFile, File xmpFile) {
        store.put(new Metric(getImage(imageFile)), XMPParser.parse(xmpFile));
    }

    public boolean isEmpty() {
        return store.isEmpty();
    }

    public XMPParams get(File imageFile) {
        List<NearMetric> nearMetrics = getNearMetrics(getImage(imageFile));
        if (nearMetrics.size() == 1) {
            return store.get(nearMetrics.get(0).getMetric());
        }
        double distSum = nearMetrics.stream().mapToDouble(NearMetric::getDistance).sum();
        double k1 = nearMetrics.get(1).getDistance() / distSum;
        double k2 = nearMetrics.get(0).getDistance() / distSum;
        XMPParams xmpParams1 = store.get(nearMetrics.get(0).getMetric());
        XMPParams xmpParams2 = store.get(nearMetrics.get(1).getMetric());
        return new XMPParams(xmpParams1, k1, xmpParams2, k2);
    }

    private List<NearMetric> getNearMetrics(BufferedImage bufferedImage) {
        Metric currentMetric = new Metric(bufferedImage);
        List<NearMetric> nearMetrics = new ArrayList<>();
        for (Metric metric: store.keySet()) {
            double dist = Metric.getDist(currentMetric, metric);
            nearMetrics.add(new NearMetric(metric, dist));
        }
        nearMetrics.sort(Comparator.comparing(NearMetric::getDistance));
        if (nearMetrics.get(0).getDistance() < 0.0001) {
            return Collections.singletonList(nearMetrics.get(0));
        }
        return nearMetrics.subList(0, 2);
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
