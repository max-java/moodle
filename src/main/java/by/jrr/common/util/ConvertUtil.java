package by.jrr.common.util;

public class ConvertUtil {
    public static Long stringToLongOrNull(String val) {
        if(val != null) {
            try {
                return Long.valueOf(val);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
}
