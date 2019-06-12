package org.aion.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import org.aion.types.InternalTransaction.RejectedStatus;
import org.junit.Test;

public class TransactionResultTest {

    @Test(expected = NullPointerException.class)
    public void testResultWithNullStatus() {
        new TransactionResult(null, Collections.emptyList(), Collections.emptyList(), 0, new byte[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testResultWithNullLogs() {
        new TransactionResult(TransactionStatus.successful(), null, Collections.emptyList(), 0, new byte[0]);
    }

    @Test(expected = NullPointerException.class)
    public void testResultWithNullInternalTransactions() {
        new TransactionResult(TransactionStatus.successful(), Collections.emptyList(), null, 0, new byte[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultWithNegativeEnergyUsed() {
        new TransactionResult(TransactionStatus.successful(), Collections.emptyList(), Collections.emptyList(), -1, new byte[0]);
    }

    @Test
    public void testNullOutput() {
        TransactionResult result = new TransactionResult(TransactionStatus.successful(), Collections.emptyList(), Collections.emptyList(), 0, null);
        assertFalse(result.copyOfTransactionOutput().isPresent());
    }

    @Test
    public void testValidTransactionResult() {
        TransactionStatus status = TransactionStatus.rejection("out of energy");
        Log log = Log.dataOnly(randomBytes(20), randomBytes(31));
        InternalTransaction internalTransaction = InternalTransaction.contractCreateTransaction(RejectedStatus.REJECTED, new AionAddress(randomBytes(AionAddress.LENGTH)), BigInteger.TEN, BigInteger.TWO.pow(12), randomBytes(120), 1_000_000, 30);
        long energyUsed = 550_000;
        byte[] output = randomBytes(30);

        TransactionResult result = new TransactionResult(status, Collections.singletonList(log), Collections.singletonList(internalTransaction), energyUsed, output);

        assertEquals(status, result.transactionStatus);
        assertEquals(Collections.singletonList(log), result.logs);
        assertEquals(Collections.singletonList(internalTransaction), result.internalTransactions);
        assertEquals(energyUsed, result.energyUsed);
        assertTrue(result.copyOfTransactionOutput().isPresent());
        assertArrayEquals(output, result.copyOfTransactionOutput().get());
    }

    @Test
    public void testImmutabilityOfOutputParameter() {
        byte[] output = new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        byte[] copyOfOriginalOutput = Arrays.copyOf(output, output.length);

        TransactionResult result = new TransactionResult(TransactionStatus.successful(), Collections.emptyList(), Collections.emptyList(), 0, output);

        // Verify modifying the array as input does not modify the array that the class returns.
        output[0] = 0xf;
        assertArrayEquals(copyOfOriginalOutput, result.copyOfTransactionOutput().get());

        // Verify modifying the returned array does not actually modify the array held by the class.
        result.copyOfTransactionOutput().get()[0] = 0xa;
        assertArrayEquals(copyOfOriginalOutput, result.copyOfTransactionOutput().get());
    }

    private static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
