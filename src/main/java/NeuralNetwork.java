import java.io.Serializable;

public class NeuralNetwork implements Serializable {

    private Neuron[] inputNeurons;

    private Neuron[] hiddenNeurons;

    private Neuron[] outputNeurons;

    private double[][] inputHiddenWeights;

    private double[][] prevInputHiddenWeightsDelta;

    private double[][] hiddenOutputWeights;

    private double[][] prevHiddenOutputWeightsDelta;

    public NeuralNetwork(int inputNeuronsCount, int hiddenNeuronsCount, int outputNeuronsCount) {
        inputNeurons = new Neuron[inputNeuronsCount];
        for (int i = 0; i < inputNeuronsCount; i++) {
            inputNeurons[i] = new Neuron();
        }

        hiddenNeurons = new Neuron[hiddenNeuronsCount];
        for (int i = 0; i < hiddenNeuronsCount; i++) {
            hiddenNeurons[i] = new Neuron();
        }

        outputNeurons = new Neuron[outputNeuronsCount];
        for (int i = 0; i < outputNeuronsCount; i++) {
            outputNeurons[i] = new Neuron();
        }

        inputHiddenWeights = new double[inputNeuronsCount][hiddenNeuronsCount];
        prevInputHiddenWeightsDelta = new double[inputNeuronsCount][hiddenNeuronsCount];
        for (int i = 0; i < inputNeuronsCount; i++) {
            for (int j = 0; j < hiddenNeuronsCount; j++) {
                inputHiddenWeights[i][j] = Math.random();
                prevInputHiddenWeightsDelta[i][j] = 0;
            }
        }

        hiddenOutputWeights = new double[hiddenNeuronsCount][outputNeuronsCount];
        prevHiddenOutputWeightsDelta = new double[hiddenNeuronsCount][outputNeuronsCount];
        for (int i = 0; i < hiddenNeuronsCount; i++) {
            for (int j = 0; j < outputNeuronsCount; j++) {
                hiddenOutputWeights[i][j] = Math.random();
                prevHiddenOutputWeightsDelta[i][j] = 0;
            }
        }
    }

    public double[] calc(double[] inputData) {
        for (int i = 0; i < inputNeurons.length; i++) {
            inputNeurons[i].setOutput(inputData[i]);
        }

        calcNextLayer(inputNeurons, hiddenNeurons, inputHiddenWeights);
        calcNextLayer(hiddenNeurons, outputNeurons, hiddenOutputWeights);

        double[] result = new double[outputNeurons.length];
        for (int i = 0; i < outputNeurons.length; i++) {
            result[i] = outputNeurons[i].getOutput();
        }
        return result;
    }

    private void calcNextLayer(Neuron[] layer, Neuron[] nextLayer, double[][] weights) {
        for (int i = 0; i < nextLayer.length; i++) {
            double sum = 0;
            for (int j = 0; j < layer.length; j++) {
                sum += layer[j].getOutput() * weights[j][i];
            }
            nextLayer[i].setInput(sum);
            nextLayer[i].setOutput(f(sum));
        }
    }

    public double updateWeights(TrainingData trainingData, double speed, double moment) {
        calc(trainingData.getInputData());
        double error = 0;
        for (int i = 0; i < outputNeurons.length; i++) {
            error += (trainingData.getOutputData()[i] - outputNeurons[i].getOutput()) * (trainingData.getOutputData()[i] - outputNeurons[i].getOutput());
        }
        double[] outputData = trainingData.getOutputData();
        double[] outputDelta = new double[outputNeurons.length];
        for (int i = 0; i < outputNeurons.length; i++) {
            double output = outputNeurons[i].getOutput();
            outputDelta[i] = (outputData[i] - output) * (1.0 - output) * output;
        }

        double[] hiddenDelta = new double[hiddenNeurons.length];
        for (int i = 0; i < hiddenNeurons.length; i++) {
            double sum = 0;
            for (int j = 0; j < outputNeurons.length; j++) {
                sum += outputDelta[j] * hiddenOutputWeights[i][j];
            }
            double output = hiddenNeurons[i].getOutput();
            hiddenDelta[i] = (1 - output) * output * sum;
        }

        calcWeights(outputNeurons, hiddenNeurons, hiddenOutputWeights, prevHiddenOutputWeightsDelta, outputDelta, speed, moment);
        calcWeights(hiddenNeurons, inputNeurons, inputHiddenWeights, prevInputHiddenWeightsDelta, hiddenDelta, speed, moment);

        return error / 12d;
    }

    private void calcWeights(Neuron[] layer, Neuron[] prevLayer, double[][] weights, double[][] deltaWeights, double[] layerDelta, double speed, double moment) {
        for (int i = 0; i < prevLayer.length; i++) {
            for (int j = 0; j < layer.length; j++) {
                double grad = layerDelta[j] * prevLayer[i].getOutput();
                double weightDelta = speed * grad + moment * deltaWeights[i][j];
                deltaWeights[i][j] = weightDelta;
                weights[i][j] = weights[i][j] + weightDelta;
            }
        }
    }

    public void reset() {
        for (int i = 0; i < inputNeurons.length; i++) {
            for (int j = 0; j < hiddenNeurons.length; j++) {
                prevInputHiddenWeightsDelta[i][j] = 0;
            }
        }
        for (int i = 0; i < hiddenNeurons.length; i++) {
            for (int j = 0; j < outputNeurons.length; j++) {
                prevHiddenOutputWeightsDelta[i][j] = 0;
            }
        }
    }

    private double f(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
