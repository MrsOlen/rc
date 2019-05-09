import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMPParser {

    private static final String CRS_TEMPERATURE = "crs:Temperature";
    private static final String CRS_TINT = "crs:Tint";
    private static final String CRS_EXPOSURE = "crs:Exposure2012";
    private static final String CRS_CONTRAST = "crs:Contrast2012";
    private static final String CRS_HIGHLIGHTS = "crs:Highlights2012";
    private static final String CRS_SHADOWS = "crs:Shadows2012";
    private static final String CRS_WHITES = "crs:Whites2012";
    private static final String CRS_BLACKS = "crs:Blacks2012";
    private static final String CRS_CLARITY = "crs:Clarity2012";
    private static final String CRS_DEHAZE = "crs:Dehaze";
    private static final String CRS_VIBRANCE = "crs:Vibrance";
    private static final String CRS_SATURATION = "crs:Saturation";

    private static final Map<String, String> propertyToMethodMap;
    private static final Map<String, Class> methodParametersMap;

    static {
        propertyToMethodMap = new HashMap<String, String>();
        propertyToMethodMap.put(CRS_TEMPERATURE, "Temperature");
        propertyToMethodMap.put(CRS_TINT, "Tint");
        propertyToMethodMap.put(CRS_EXPOSURE, "Exposure");
        propertyToMethodMap.put(CRS_CONTRAST, "Contrast");
        propertyToMethodMap.put(CRS_HIGHLIGHTS, "Highlights");
        propertyToMethodMap.put(CRS_SHADOWS, "Shadows");
        propertyToMethodMap.put(CRS_WHITES, "Whites");
        propertyToMethodMap.put(CRS_BLACKS, "Blacks");
        propertyToMethodMap.put(CRS_CLARITY, "Clarity");
        propertyToMethodMap.put(CRS_DEHAZE, "Dehaze");
        propertyToMethodMap.put(CRS_VIBRANCE, "Vibrance");
        propertyToMethodMap.put(CRS_SATURATION, "Saturation");

        methodParametersMap = new HashMap<String, Class>();
        methodParametersMap.put("Temperature", int.class);
        methodParametersMap.put("Tint", int.class);
        methodParametersMap.put("Exposure", double.class);
        methodParametersMap.put("Contrast", int.class);
        methodParametersMap.put("Highlights", int.class);
        methodParametersMap.put("Shadows", int.class);
        methodParametersMap.put("Whites", int.class);
        methodParametersMap.put("Blacks", int.class);
        methodParametersMap.put("Clarity", int.class);
        methodParametersMap.put("Dehaze", int.class);
        methodParametersMap.put("Vibrance", int.class);
        methodParametersMap.put("Saturation", int.class);
    }

    public static XMPParams parse(File file) {
        try {
            XMPParams xmpParams = new XMPParams();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                addXMPParams(xmpParams, line);
                line = reader.readLine();
            }
            reader.close();
            return xmpParams;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static String getReplacedFileContent(File sourceFile, XMPParams xmpParams) {
        try {
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            List<String> replacedProperties = new ArrayList<>();
            String line = reader.readLine();
            String nextLine = reader.readLine();
            while (!line.contains("crs:") || nextLine.contains("crs:")) {
                fileContent.append(getReplacedLine(line, xmpParams, replacedProperties)).append("\"\n");
                line = nextLine;
                nextLine = reader.readLine();
            }
            for (String property: propertyToMethodMap.keySet()) {
                if (!replacedProperties.contains(property)) {
                    try {
                        String methodName = "get" + propertyToMethodMap.get(property);
                        Method method = xmpParams.getClass().getMethod(methodName);
                        String propertyValue = method.invoke(xmpParams, null).toString();
                        fileContent.append(String.format("   %s=\"%s\"\n", property, propertyValue));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            while (line != null) {
                fileContent.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static String getReplacedLine(String line, XMPParams xmpParams, List<String> replacedProperties) {
        for (String property : propertyToMethodMap.keySet()) {
            if (line.lastIndexOf(property) != -1) {
                try {
                    replacedProperties.add(property);
                    String methodName = "get" + propertyToMethodMap.get(property);
                    Method method = xmpParams.getClass().getMethod(methodName);
                    String[] lineParts = line.split("\"");
                    lineParts[lineParts.length - 1] = method.invoke(xmpParams, null).toString();
                    return String.join("\"", lineParts);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return line;
    }

    private static void addXMPParams(XMPParams xmpParams, String line) {
        for (String property : propertyToMethodMap.keySet()) {
            if (line.lastIndexOf(property) != -1) {
                try {
                    String methodName = propertyToMethodMap.get(property);
                    Class paramClass = methodParametersMap.get(methodName);
                    Method method = xmpParams.getClass().getMethod("set" + methodName, paramClass);
                    String param = line.split("\"")[1];
                    if (double.class.equals(paramClass)) {
                        method.invoke(xmpParams, Double.parseDouble(param));
                    } else {
                        method.invoke(xmpParams, Integer.parseInt(param));
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
