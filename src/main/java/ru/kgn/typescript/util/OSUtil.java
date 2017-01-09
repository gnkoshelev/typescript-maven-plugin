package ru.kgn.typescript.util;

/**
 * @author Gregory [KGN]
 */
public class OSUtil {
    public static String getOSName () {
        return System.getProperty("os.name");
    }

    public static boolean isWindows() {
        return getOSName().startsWith("Windows");
    }
}
