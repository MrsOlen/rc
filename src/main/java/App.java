import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Не задан каталог с изображениями");
            return;
        }

        String imageDirectory = args[0];

        NeuralNetwork neuralNetwork = getNetwork();

        if (args.length > 1) {
            String sourceDirectory = args[1];
            learnNetwork(neuralNetwork, sourceDirectory);
        }

        saveNetwork(neuralNetwork);

        processImages(neuralNetwork, imageDirectory);
    }

    public static NeuralNetwork getNetwork() {
        String userDirectory = System.getProperty("user.home");
        File directory = new File(userDirectory + "\\RawConverter");
        if (!directory.exists()) {
            return new NeuralNetwork(36, 12, 12);
        }
        File file = new File(userDirectory + "\\RawConverter\\weights.txt");
        if (!file.exists()) {
            return new NeuralNetwork(36, 12, 12);
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (NeuralNetwork) ois.readObject();
        } catch (Exception e) {
            System.out.println("Произошла ошибка при загрузке нейросети");
            return new NeuralNetwork(36, 12, 12);
        }
    }

    public static void saveNetwork(NeuralNetwork neuralNetwork) {
        String userDirectory = System.getProperty("user.home");
        File directory = new File(userDirectory + "\\RawConverter");
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            FileOutputStream fot = new FileOutputStream(new File(userDirectory + "\\RawConverter\\weights.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fot);
            oos.writeObject(neuralNetwork);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void learnNetwork(NeuralNetwork neuralNetwork, String sourceDirectoryName) {
        File sourceDirectory = new File(sourceDirectoryName);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new RuntimeException(sourceDirectory.getAbsolutePath() + " не является директорией");
        }
        List<String> imageFileNames = new ArrayList<>();
        for (File file : sourceDirectory.listFiles(file -> {
            String fileName = file.getName().toUpperCase();
            return fileName.contains("NEF") || fileName.contains("ARW");
        })) {
            imageFileNames.add(file.getAbsolutePath());
        }
        NetworkLearner networkLearner = new NetworkLearner(neuralNetwork, 0.4, 0.3, 100000);

        List<TrainingData> trainingSet = new ArrayList<>();
        for (String imageFileName : imageFileNames) {
            trainingSet.add(new TrainingData(new File(imageFileName), new File(getXmpFileName(imageFileName))));
        }
        networkLearner.learn(trainingSet);
    }

    private static void processImages(NeuralNetwork neuralNetwork, String imageDirectoryFileName) {
        File imageDirectory = new File(imageDirectoryFileName);
        if (!imageDirectory.exists() || !imageDirectory.isDirectory()) {
            throw new RuntimeException(imageDirectory.getAbsolutePath() + " не является директорией");
        }
        for (File imageFile : imageDirectory.listFiles(file -> {
            String fileName = file.getName().toUpperCase();
            return fileName.contains("NEF") || fileName.contains("ARW");
        })) {
            List<Double> inputVector = new Metric(imageFile).getNormalizedVector();
            double[] inputData = new double[inputVector.size()];
            for (int i = 0; i < inputVector.size(); i++) {
                inputData[i] = inputVector.get(i);
            }

            double[] outputData = neuralNetwork.calc(inputData);

            List<Double> outputVector = new ArrayList<>();
            for (double outputDatum : outputData) {
                outputVector.add(outputDatum);
            }
            XMPParams xmpParams = new XMPParams(outputVector);

            String xmpFileName = getXmpFileName(imageFile.getAbsolutePath());
            File xmpFile = new File(xmpFileName);
            if (xmpFile.exists()) {
                if (!xmpFile.renameTo(new File(getXmpSourceFileName(xmpFileName)))) {
                    throw new RuntimeException("Не удалось переименовать файл " + xmpFileName);
                }
                try {
                    FileWriter fileWriter = new FileWriter(xmpFileName);
                    fileWriter.write(XMPParser.getReplacedFileContent(new File(getXmpSourceFileName(xmpFileName)), xmpParams));
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Ошибка записи файла " + imageFile.getAbsolutePath());
                }
            } else {
                try {
                    FileWriter newFileWriter = new FileWriter(xmpFileName);
                    newFileWriter.write(XMPParser.getFileContent(xmpParams, imageFile.getName()));
                    newFileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getXmpFileName(String imageFileName) {
        String[] fileParts = imageFileName.split("\\.");
        fileParts[fileParts.length - 1] = "xmp";
        return String.join(".", fileParts);
    }

    private static String getXmpSourceFileName(String xmpFileName) {
        String[] fileParts = xmpFileName.split("\\.");
        fileParts[fileParts.length - 2] = fileParts[fileParts.length - 2] + "-s";
        fileParts[fileParts.length - 1] = "xmp";
        return String.join(".", fileParts);
    }
}
