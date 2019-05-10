public class NearMetric {

    private Metric metric;
    private Double distance;

    public NearMetric(Metric metric, double distance) {
        this.metric = metric;
        this.distance = distance;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
