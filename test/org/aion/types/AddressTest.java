package org.aion.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Test;


public class AddressTest {

    @Test(expected = NullPointerException.class)
    public void testNullAddress() {
        AionAddress address = new AionAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongLengthAddress() {
        byte[] wrongLength = new byte[20];
        AionAddress address = new AionAddress(wrongLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAddress() {
        byte[] wrongLength = new byte[0];
        AionAddress address = new AionAddress(wrongLength);
    }

    @Test
    public void testToString() {
        byte[] data = createByteArray(0);
        byte[] data1 = createByteArray(1);

        String result = new AionAddress(data).toString();
        assertEquals("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", result);

        result = new AionAddress(data1).toString();
        assertEquals("0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20", result);
    }

    @Test
    public void testEquals() {
        byte[] data = createByteArray(0);
        byte[] data1 = createByteArray(1);

        boolean result = new AionAddress(data).equals(new AionAddress(data));
        Assert.assertTrue(result);

        result = new AionAddress(data).equals(new AionAddress(data1));
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode() {
        AionAddress address0 = new AionAddress(createByteArray(0));
        AionAddress address1 = new AionAddress(createByteArray(1));

        AionAddress address0copy = new AionAddress(createByteArray(0));
        AionAddress address1copy = new AionAddress(createByteArray(1));

        assertEquals(address0.hashCode(), address0copy.hashCode());
        assertEquals(address1.hashCode(), address1copy.hashCode());
    }

    @Test
    public void testImmutableCreation() {
        byte[] data = createByteArray(0);
        AionAddress address1 = new AionAddress(data);
        AionAddress address2 = new AionAddress(data);

        // These addresses were created from identical bytes, and so are equal
        assertEquals(address1, address2);
        assertArrayEquals(address1.toByteArray(), address2.toByteArray());

        byte[] dataCopy = new byte[AionAddress.LENGTH];
        System.arraycopy(data, 0, dataCopy, 0, AionAddress.LENGTH);
        data[0] = (byte) ~data[0];
        AionAddress address3 = new AionAddress(data);

        // Changing the byte array from which address1 was created should not change address1
        assertNotEquals(address1, address3);
        assertArrayEquals(dataCopy, address1.toByteArray());
    }

    @Test
    public void testImmutableToByteArray() {
        byte[] data = createByteArray(0);
        AionAddress address1 = new AionAddress(data);
        AionAddress address2 = new AionAddress(data);

        // These addresses were created from identical bytes, and so are equal
        assertEquals(address1, address2);

        data = address1.toByteArray();
        data[0] = (byte) ~data[0];

        // address1 and address2 should still be equal despite us changing the result of toByteArray()
        assertEquals(address1, address2);
    }

    private byte[] createByteArray(int startValue) {
        byte[] data = new byte[32];
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte)(i + startValue);
        }

        return data;
    }

}