package org.aion.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.math.BigInteger;
import java.util.Arrays;
import org.aion.types.test_util.AddressUtil;
import org.junit.Before;
import org.junit.Test;


public class TransactionTest {

    private AionAddress senderAddress;
    private AionAddress destinationAddress;
    private byte[] transactionHash;
    private BigInteger value;
    private BigInteger nonce;
    private long energyPrice;
    private long energyLimit;
    private boolean isCreate;
    private byte[] transactionData;

    @Before
    public void setup() {
        senderAddress = AddressUtil.randomAddress();
        destinationAddress = AddressUtil.randomAddress();
        // For now we're simply providing 32 random bytes as the "hash"
        transactionHash = AddressUtil.randomAddress().toByteArray();
        value = BigInteger.ZERO;
        nonce = BigInteger.ZERO;
        energyPrice = 100_000L;
        energyLimit = 1L;
        isCreate = false;
        // For now we're simply providing 32 random bytes as the "data"
        transactionData = AddressUtil.randomAddress().toByteArray();
    }
    
    @Test
    public void testConstructor() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
        assertEquals(tx.senderAddress, senderAddress);
        assertEquals(tx.destinationAddress, destinationAddress);
        assertEquals(tx.value, value);
        assertEquals(tx.nonce, nonce);
        assertEquals(tx.energyPrice, energyPrice);
        assertEquals(tx.energyLimit, energyLimit);
        assertEquals(tx.isCreate, isCreate);        
        assertArrayEquals(tx.copyOfTransactionHash(), transactionHash);
        assertArrayEquals(tx.copyOfTransactionData(), transactionData);
    }

    @Test
    public void testEquals() {
        Transaction tx1 = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
        Transaction tx2 = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);

        assertEquals(tx1, tx2);
        assertEquals(tx1.senderAddress, tx2.senderAddress);
        assertEquals(tx1.destinationAddress, tx2.destinationAddress);
        assertArrayEquals(tx1.copyOfTransactionHash(), tx2.copyOfTransactionHash());
        assertEquals(tx1.value, tx2.value);
        assertEquals(tx1.nonce, tx2.nonce);
        assertEquals(tx1.energyPrice, tx2.energyPrice);
        assertEquals(tx1.energyLimit, tx2.energyLimit);
        assertEquals(tx1.isCreate, tx2.isCreate);
        assertArrayEquals(tx1.copyOfTransactionData(), tx2.copyOfTransactionData());
    }

    @Test
    public void testHashCode() {
        Transaction tx1 = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
        Transaction tx2 = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);

        assertEquals(tx1.hashCode(), tx2.hashCode());

        byte[] senderAddressBytes = senderAddress.toByteArray();
        senderAddressBytes[0] = (byte) ~senderAddressBytes[0];
        tx2 = new Transaction(new AionAddress(senderAddressBytes), destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
        assertNotEquals(tx1.hashCode(), tx2.hashCode());
    }

    @Test
    public void testImmutablity() {
        Transaction tx1 = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);

        // changing the source array of the transactionHash shouldn't change the Transaction object
        transactionHash[0] = (byte) ~transactionHash[0];
        assertFalse(Arrays.equals(transactionHash, tx1.copyOfTransactionHash()));

        // changing the source array of the transactionData shouldn't change the Transaction object
        transactionData[0] = (byte) ~transactionData[0];
        assertFalse(Arrays.equals(transactionData, tx1.copyOfTransactionData()));

        byte[] transactionHashFromObject = tx1.copyOfTransactionHash();
        byte[] transactionDataFromObject = tx1.copyOfTransactionData();

        // messing with the array returned from the getter shouldn't change the Transaction object
        transactionDataFromObject[0] = (byte) ~transactionDataFromObject[0];
        assertFalse(Arrays.equals(transactionDataFromObject, tx1.copyOfTransactionData()));

        // messing with the array returned from the getter shouldn't change the Transaction object
        transactionHashFromObject[0] = (byte) ~transactionHashFromObject[0];
        assertFalse(Arrays.equals(transactionHashFromObject, tx1.copyOfTransactionHash()));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSender() {
        Transaction tx = new Transaction(null, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
    }

    @Test
    public void testNullDestination() {
        Transaction tx = new Transaction(senderAddress, null, transactionHash, value, nonce, energyPrice, energyLimit, transactionData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullHash() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, null, value, nonce, energyPrice, energyLimit, transactionData);
    }

    @Test(expected = NullPointerException.class)
    public void testNullData() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, energyLimit, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, BigInteger.valueOf(-1), nonce, energyPrice, energyLimit, transactionData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNonce() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, value, BigInteger.valueOf(-1), energyPrice, energyLimit, transactionData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnergyPrice() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, -1, energyLimit, transactionData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnergyLimit() {
        Transaction tx = new Transaction(senderAddress, destinationAddress, transactionHash, value, nonce, energyPrice, -1, transactionData);
    }

}