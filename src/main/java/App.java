import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static Map<Metric, String> metricFileMap;

    private static void fillMetricFileMap(String filePath) {
        metricFileMap = new HashMap<Metric, String>();
    }

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        System.out.println(filePath);
        fillMetricFileMap(filePath);
        BufferedImage image = getImage(filePath);
        Metric nearMetric = getNearMetric(image);
        String fileName = metricFileMap.get(nearMetric);
        XMPParams xmpParams = XMPParser.parse(new File(filePath + fileName));
        System.out.println(xmpParams);
    }

    private static Metric getNearMetric(BufferedImage bufferedImage) {
        Metric currentMetric = new Metric(bufferedImage);
        int minDist = Integer.MAX_VALUE;
        Metric result = null;
        for (Metric metric: metricFileMap.keySet()) {
            int dist = Metric.getDist(currentMetric, metric);
            if (dist < minDist) {
                minDist = dist;
                result = metric;
            }
        }
        return result;
    }

    private static BufferedImage getImage(String fileName) {
        try {
            File file = new File(fileName);
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
