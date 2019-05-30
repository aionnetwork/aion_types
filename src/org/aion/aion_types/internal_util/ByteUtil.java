package org.aion.aion_types.internal_util;

public final class ByteUtil {

    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("Cannot convert null to string!");
        }

        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
