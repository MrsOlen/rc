import java.io.File;
import java.util.List;

public class TrainingData {

    private double[] inputData;
    private double[] outputData;

    public TrainingData(File imageFile, File xmpFile) {
        List<Double> inputVector = new Metric(imageFile).getNormalizedVector();
        inputData = new double[inputVector.size()];
        for (int i = 0; i < inputVector.size(); i++) {
            inputData[i] = inputVector.get(i);
        }

        List<Double> outputVector = XMPParser.parse(xmpFile).getNormalizedVector();
        outputData = new double[outputVector.size()];
        for (int i = 0; i < outputVector.size(); i++) {
            outputData[i] = outputVector.get(i);
        }
    }

    public double[] getInputData() {
        return inputData;
    }

    public void setInputData(double[] inputData) {
        this.inputData = inputData;
    }

    public double[] getOutputData() {
        return outputData;
    }

    public void setOutputData(double[] outputData) {
        this.outputData = outputData;
    }
}
