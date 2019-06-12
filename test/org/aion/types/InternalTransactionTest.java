package org.aion.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.Arrays;
import org.aion.types.InternalTransaction.RejectedStatus;
import org.junit.Test;

public class InternalTransactionTest {
    private static final RejectedStatus STATUS = RejectedStatus.NOT_REJECTED;
    private static final AionAddress SENDER = new AionAddress(new byte[AionAddress.LENGTH]);
    private static final AionAddress DESTINATION = new AionAddress(new byte[AionAddress.LENGTH]);
    private static final BigInteger NONCE = BigInteger.ZERO;
    private static final BigInteger VALUE = BigInteger.TEN;
    private static final byte[] DATA = new byte[42];
    private static final long LIMIT = 1_222_333;
    private static final long PRICE = 12;

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullStatus() {
        InternalTransaction.contractCreateTransaction(null, SENDER, NONCE, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullSender() {
        InternalTransaction.contractCreateTransaction(STATUS, null, NONCE, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullNonce() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, null, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeNonce() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, BigInteger.ONE.negate(), VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullValue() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, null, DATA, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeValue() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, BigInteger.ONE.negate(), DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullData() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, null, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeEnergyLimit() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, DATA, -1, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeEnergyPrice() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, DATA, LIMIT, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEnergyPriceZero() {
        InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, DATA, LIMIT, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullStatus() {
        InternalTransaction.contractCallTransaction(null, SENDER, DESTINATION, NONCE, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullSender() {
        InternalTransaction.contractCallTransaction(STATUS, null, DESTINATION, NONCE, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullDestination() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, null, NONCE, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullNonce() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, null, VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithNegativeNonce() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, BigInteger.ONE.negate(), VALUE, DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullValue() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, null, DATA, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithNegativeValue() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, BigInteger.ONE.negate(), DATA, LIMIT, PRICE);
    }

    @Test(expected = NullPointerException.class)
    public void testCallWithNullData() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, null, LIMIT, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithNegativeEnergyLimit() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, DATA, -1, PRICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithNegativeEnergyPrice() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, DATA, LIMIT, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallWithEnergyPriceZero() {
        InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, DATA, LIMIT, 0);
    }

    @Test
    public void testValidCreateTransaction() {
        InternalTransaction internalTransaction = InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, DATA, LIMIT, PRICE);

        assertEquals(STATUS == RejectedStatus.REJECTED, internalTransaction.isRejected);
        assertEquals(SENDER, internalTransaction.sender);
        assertEquals(NONCE, internalTransaction.senderNonce);
        assertEquals(VALUE, internalTransaction.value);
        assertEquals(LIMIT, internalTransaction.energyLimit);
        assertEquals(PRICE, internalTransaction.energyPrice);
        assertArrayEquals(DATA, internalTransaction.copyOfData());
        assertNull(internalTransaction.destination);
    }

    @Test
    public void testValidCallTransaction() {
        InternalTransaction internalTransaction = InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, DATA, LIMIT, PRICE);

        assertEquals(STATUS == RejectedStatus.REJECTED, internalTransaction.isRejected);
        assertEquals(SENDER, internalTransaction.sender);
        assertEquals(DESTINATION, internalTransaction.destination);
        assertEquals(NONCE, internalTransaction.senderNonce);
        assertEquals(VALUE, internalTransaction.value);
        assertEquals(LIMIT, internalTransaction.energyLimit);
        assertEquals(PRICE, internalTransaction.energyPrice);
        assertArrayEquals(DATA, internalTransaction.copyOfData());
    }

    @Test
    public void testImmutabilityOfInputData() {
        byte[] data = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        byte[] copyOfData = Arrays.copyOf(data, data.length);

        InternalTransaction createTransaction = InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, data, LIMIT, PRICE);
        InternalTransaction callTransaction = InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, data, LIMIT, PRICE);

        // Modify the data we passed in and verify the transactions did not get modified.
        data[0] = (byte) ~data[0];

        assertArrayEquals(copyOfData, createTransaction.copyOfData());
        assertArrayEquals(copyOfData, callTransaction.copyOfData());
    }

    @Test
    public void testImmutabilityOfReturnedData() {
        byte[] data = new byte[]{ 9, 6, 6, 4, 3, 1, 0, 0, 1, 9, 2 };
        byte[] copyOfData = Arrays.copyOf(data, data.length);

        InternalTransaction createTransaction = InternalTransaction.contractCreateTransaction(STATUS, SENDER, NONCE, VALUE, data, LIMIT, PRICE);
        InternalTransaction callTransaction = InternalTransaction.contractCallTransaction(STATUS, SENDER, DESTINATION, NONCE, VALUE, data, LIMIT, PRICE);

        // Modify the returned data and verify the transactions did not get modified.
        createTransaction.copyOfData()[0] = 0xf;
        callTransaction.copyOfData()[0] = 0xf;

        assertArrayEquals(copyOfData, createTransaction.copyOfData());
        assertArrayEquals(copyOfData, callTransaction.copyOfData());
    }
}
