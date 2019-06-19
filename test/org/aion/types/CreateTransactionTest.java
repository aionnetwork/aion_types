package org.aion.types;

import org.aion.types.AionAddress;
import org.aion.types.Transaction;
import org.aion.types.test_util.AddressUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.*;


public class CreateTransactionTest {

    private AionAddress senderAddress;
    private byte[] transactionHash;
    private BigInteger value;
    private BigInteger nonce;
    private long energyPrice;
    private long energyLimit;
    private byte[] transactionData;

    @Before
    public void setup() {
        senderAddress = AddressUtil.randomAddress();
        // For now we're simply providing 32 random bytes as the "hash"
        transactionHash = AddressUtil.randomAddress().toByteArray();
        value = BigInteger.ZERO;
        nonce = BigInteger.ZERO;
        energyPrice = 100_000L;
        energyLimit = 1L;
        // For now we're simply providing 32 random bytes as the "data"
        transactionData = AddressUtil.randomAddress().toByteArray();
    }
    
    @Test
    public void testConstructor() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);
        assertEquals(tx.senderAddress, senderAddress);
        assertEquals(tx.value, value);
        assertEquals(tx.nonce, nonce);
        assertEquals(tx.energyPrice, energyPrice);
        assertEquals(tx.energyLimit, energyLimit);
        assertTrue(tx.isCreate);
        assertArrayEquals(tx.copyOfTransactionHash(), transactionHash);
        assertArrayEquals(tx.copyOfTransactionData(), transactionData);
    }

    @Test
    public void testEquals() {
        Transaction tx1 = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);
        Transaction tx2 = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);

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
        Transaction tx1 = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);
        Transaction tx2 = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);

        assertEquals(tx1.hashCode(), tx2.hashCode());

        byte[] senderAddressBytes = senderAddress.toByteArray();
        senderAddressBytes[0] = (byte) ~senderAddressBytes[0];
        tx2 = Transaction.contractCreateTransaction(new AionAddress(senderAddressBytes), transactionHash, nonce, value, transactionData, energyLimit, energyPrice);
        assertNotEquals(tx1.hashCode(), tx2.hashCode());
    }

    @Test
    public void testImmutablity() {
        Transaction tx1 = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);

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
        Transaction tx = Transaction.contractCreateTransaction(null, transactionHash, nonce, value, transactionData, energyLimit, energyPrice);
    }

    @Test(expected = NullPointerException.class)
    public void testNullHash() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, null, nonce, value, transactionData, energyLimit, energyPrice);
    }

    @Test(expected = NullPointerException.class)
    public void testNullData() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, null, energyLimit, energyPrice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, BigInteger.valueOf(-1), transactionData, energyLimit, energyPrice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNonce() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, BigInteger.valueOf(-1), value, transactionData, energyLimit, energyPrice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnergyPrice() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, energyLimit, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEnergyLimit() {
        Transaction tx = Transaction.contractCreateTransaction(senderAddress, transactionHash, nonce, value, transactionData, -1, energyPrice);
    }

}