import java.util.List;

public class NetworkLearner {

    private NeuralNetwork neuralNetwork;
    private double speed;
    private double moment;
    private int epochCount;

    public NetworkLearner(NeuralNetwork neuralNetwork, double speed, double moment, int epochCount) {
        this.neuralNetwork = neuralNetwork;
        this.speed = speed;
        this.moment = moment;
        this.epochCount = epochCount;
    }

    public void learn (List<TrainingData> trainingSet) {
        int count = 0;

        double maxError;
        do {
            neuralNetwork.reset();
            maxError = 0;
            for (TrainingData trainingData: trainingSet) {
                double error = neuralNetwork.updateWeights(trainingData, speed, moment);
                if (error > maxError) {
                    maxError = error;
                }
            }
            count++;
        } while (maxError > 0.0001 && count < epochCount);
        System.out.println(String.format("Сеть прошла обучение %s раз, ошибка = %.5f%n", count, maxError));
    }
}
