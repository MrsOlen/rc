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

        String sourceDirectory = "";
        if (args.length > 1) {
            sourceDirectory = args[1];
        }

        Store store = getStore(sourceDirectory);
        if (store.isEmpty()) {
            System.out.println("Хранилище с образцами обработки пусто");
            return;
        }
        processImages(imageDirectory, store);
        //saveStore(store);

    }

    private static Store getStore(String sourceDirectoryName) {
        Store store;
        String userDirectory = System.getProperty("user.home");
        File file = new File(userDirectory + "\\RawConverter\\store.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            store = (Store) ois.readObject();
        } catch (Exception e) {
            System.out.println("Произошла ошибка при загрузке хранилища");
            store = new Store();
        }

        File sourceDirectory = new File(sourceDirectoryName);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            return store;
        }
        List<String> imageFileNames = new ArrayList<>();
        for (File sourceFile : sourceDirectory.listFiles(sourceFile -> {
            String fileName = sourceFile.getName().toUpperCase();
            return fileName.contains("NEF") || fileName.contains("ARW");
        })) {
            imageFileNames.add(sourceFile.getAbsolutePath());
        }
        for (String imageFileName : imageFileNames) {
            store.add(new File(imageFileName), new File(getXmpFileName(imageFileName)));
        }
        return store;
    }

    public static void saveStore(Store store) {
        String userDirectory = System.getProperty("user.home");
        File directory = new File(userDirectory + "\\RawConverter");
        if (!directory.exists()) {
            directory.mkdir();
        }
        try {
            FileOutputStream fot = new FileOutputStream(new File(userDirectory + "\\RawConverter\\store.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fot);
            oos.writeObject(store);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processImages(String imageDirectoryFileName, Store store) {
        File imageDirectory = new File(imageDirectoryFileName);
        if (!imageDirectory.exists() || !imageDirectory.isDirectory()) {
            throw new RuntimeException(imageDirectory.getAbsolutePath() + " не является директорией");
        }
        for (File imageFile : imageDirectory.listFiles(file -> {
            String fileName = file.getName().toUpperCase();
            return fileName.contains("NEF") || fileName.contains("ARW");
        })) {
            XMPParams xmpParams = store.get(imageFile);
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
