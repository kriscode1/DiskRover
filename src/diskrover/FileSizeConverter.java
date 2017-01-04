/*
 * Converts bytes of file size to KB, MB, GB, or TB.
 */
package diskrover;

/**
 *
 * @author Kristofer
 */
public class FileSizeConverter {
    public static String convertFileSize(final long bytes) {
        if (bytes < 1024L) {
            //Use units of bytes
            return String.format("%d bytes", bytes);
        } else if (bytes < 1048576L) {
            //Use units of KB
            return String.format("%.2f KB", (float) bytes/1024L);
        } else if (bytes < 1073741824L) {
            //Use units of MB
            return String.format("%.2f MB", (float) bytes/1048576L);
        } else if (bytes < 1099511627776L) {
            //Writing in GB
            return String.format("%.2f GB", (float) bytes/1073741824L);
        } else {
            //Writing in TB
            return String.format("%.2f TB", (float) bytes/1099511627776L);
        }
    }
    public static String calculateSizePercent(final long fileBytes, final long totalBytes) {
        float percent = (float) (fileBytes * 100)/totalBytes;
        return String.format("% 6.2f%%", percent);
    }
}
