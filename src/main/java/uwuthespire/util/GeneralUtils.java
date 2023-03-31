package uwuthespire.util;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static String lastChars(String str, int numChars) {
        if (str.length() <= numChars) {
            return str;
        }

        return str.substring(str.length() - numChars);
    }

    public static String allButLastChars(String str, int numChars) {
        if (str.length() <= numChars) {
            return "";
        }

        return str.substring(0, str.length() - numChars);
    }
}
