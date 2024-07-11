package codesquad.server.util;

public class ContentTypeFormatter {

    private ContentTypeFormatter() {
    }

    public static String formatContentType(String fileExtension) {
        return switch (fileExtension.substring(fileExtension.lastIndexOf("."))) {
            case ".html" -> "text/html";
            case ".css" -> "text/css";
            case ".js" -> "text/javascript";
            case ".ico" -> "image/x-icon";
            case ".svg" -> "image/svg+xml";
            case ".png" -> "image/png";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".gif" -> "image/gif";
            case ".pdf" -> "application/pdf";
            case ".json" -> "application/json";
            case ".xml" -> "application/xml";
            case ".zip" -> "application/zip";
            case ".mp3" -> "audio/mpeg";
            case ".mp4" -> "video/mp4";
            case ".webm" -> "video/webm";
            case ".webp" -> "image/webp";
            default -> "text/plain";
        };
    }
}
