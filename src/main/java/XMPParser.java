import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        propertyToMethodMap.put(CRS_TEMPERATURE, "setTemperature");
        propertyToMethodMap.put(CRS_TINT, "setTint");
        propertyToMethodMap.put(CRS_EXPOSURE, "setExposure");
        propertyToMethodMap.put(CRS_CONTRAST, "setContrast");
        propertyToMethodMap.put(CRS_HIGHLIGHTS, "setHighlights");
        propertyToMethodMap.put(CRS_SHADOWS, "setShadows");
        propertyToMethodMap.put(CRS_WHITES, "setWhites");
        propertyToMethodMap.put(CRS_BLACKS, "setBlacks");
        propertyToMethodMap.put(CRS_CLARITY, "setClarity");
        propertyToMethodMap.put(CRS_DEHAZE, "setDehaze");
        propertyToMethodMap.put(CRS_VIBRANCE, "setVibrance");
        propertyToMethodMap.put(CRS_SATURATION, "setSaturation");

        methodParametersMap = new HashMap<String, Class>();
        methodParametersMap.put("setTemperature", int.class);
        methodParametersMap.put("setTint", int.class);
        methodParametersMap.put("setExposure", double.class);
        methodParametersMap.put("setContrast", int.class);
        methodParametersMap.put("setHighlights", int.class);
        methodParametersMap.put("setShadows", int.class);
        methodParametersMap.put("setWhites", int.class);
        methodParametersMap.put("setBlacks", int.class);
        methodParametersMap.put("setClarity", int.class);
        methodParametersMap.put("setDehaze", int.class);
        methodParametersMap.put("setVibrance", int.class);
        methodParametersMap.put("setSaturation", int.class);
    }

    public static XMPParams parse(File file) throws IOException {
        XMPParams xmpParams = new XMPParams();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            addXMPParams(xmpParams, line);
            line = reader.readLine();
        }
        return xmpParams;
    }

    private static void addXMPParams(XMPParams xmpParams, String line) {
        for (String property: propertyToMethodMap.keySet()) {
            if (line.lastIndexOf(property) != -1) {
                try {
                    String methodName = propertyToMethodMap.get(property);
                    Class paramClass =  methodParametersMap.get(methodName);
                    Method method = xmpParams.getClass().getMethod(methodName, paramClass);
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
