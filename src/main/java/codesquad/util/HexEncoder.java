package codesquad.util;

public class HexEncoder {

    private HexEncoder() {
    }

    public static String byteToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
