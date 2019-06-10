package org.aion.aion_types;

import java.util.Arrays;
import org.aion.aion_types.internal_util.ByteUtil;

/**
 * Represents an address of a contract or account in the Aion Network.
 */
public class AionAddress {

    /**
     * The length of an address.
     */
    public static final int LENGTH = 32;

    private final byte[] raw = new byte[LENGTH];

    /**
     * Create an Address with the contents of the given raw byte array.
     *
     * @param raw a byte array
     * @throws NullPointerException when the input byte array is null.
     * @throws IllegalArgumentException when the input byte array length is invalid.
     */
    public AionAddress(byte[] raw) throws IllegalArgumentException {
        if (raw == null) {
            throw new NullPointerException();
        }
        if (raw.length != LENGTH) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(raw, 0, this.raw, 0, LENGTH);
    }

    /**
     * Converts the receiver to a new byte array.
     *
     * @return a byte array containing a copy of the receiver.
     */
    public byte[] toByteArray() {
        byte[] copy = new byte[LENGTH];
        System.arraycopy(this.raw, 0, copy, 0, LENGTH);
        return copy;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(raw);
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = this == obj;
        if (!isEqual && (obj instanceof AionAddress)) {
            AionAddress other = (AionAddress) obj;
            isEqual = Arrays.equals(this.raw, other.raw);
        }
        return isEqual;
    }

    @Override
    public java.lang.String toString() {
        return ByteUtil.bytesToString(this.raw);
    }
}
