import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static Store store;

    public static void main(String[] args) throws IOException {
        String sourceDirectory = "C:\\Users\\Алена\\Desktop\\diploma\\test2\\source";
        String imageDirectory = "C:\\Users\\Алена\\Desktop\\diploma\\test2\\images";
        fillStore(sourceDirectory);
        processImages(imageDirectory);
    }

    private static void fillStore(String sourceDirectoryName) {
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
        store = new Store();
        for (String imageFileName : imageFileNames) {
            store.add(new File(imageFileName), new File(getXmpFileName(imageFileName)));
        }
    }

    private static void processImages(String imageDirectoryFileName) {
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
            if (!new File(xmpFileName).renameTo(new File(getXmpSourceFileName(xmpFileName)))) {
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
