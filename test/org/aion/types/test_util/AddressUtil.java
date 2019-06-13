package org.aion.types.test_util;

import java.security.SecureRandom;
import org.aion.types.AionAddress;

public class AddressUtil {
    private static SecureRandom secureRandom = new SecureRandom();

    public static AionAddress randomAddress() {
        byte[] bytes = new byte[AionAddress.LENGTH];
        secureRandom.nextBytes(bytes);
        return new AionAddress(bytes);
    }
}
