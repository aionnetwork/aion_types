package org.aion.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TransactionStatusTest {

    @Test
    public void testSuccessfulStatus() {
        TransactionStatus transactionStatus = TransactionStatus.successful();
        assertTrue(transactionStatus.isSuccess());
        assertFalse(transactionStatus.isRejected());
        assertFalse(transactionStatus.isFailed());
        assertFalse(transactionStatus.isReverted());
        assertFalse(transactionStatus.isFatal());
    }

    @Test
    public void testRejectedStatus() {
        TransactionStatus transactionStatus = TransactionStatus.rejection("rejected");
        assertFalse(transactionStatus.isSuccess());
        assertTrue(transactionStatus.isRejected());
        assertFalse(transactionStatus.isFailed());
        assertFalse(transactionStatus.isReverted());
        assertFalse(transactionStatus.isFatal());
    }

    @Test
    public void testNonRevertFailedStatus() {
        TransactionStatus transactionStatus = TransactionStatus.nonRevertedFailure("failed");
        assertFalse(transactionStatus.isSuccess());
        assertFalse(transactionStatus.isRejected());
        assertTrue(transactionStatus.isFailed());
        assertFalse(transactionStatus.isReverted());
        assertFalse(transactionStatus.isFatal());
    }

    @Test
    public void testRevertFailedStatus() {
        TransactionStatus transactionStatus = TransactionStatus.revertedFailure();
        assertFalse(transactionStatus.isSuccess());
        assertFalse(transactionStatus.isRejected());
        assertTrue(transactionStatus.isFailed());
        assertTrue(transactionStatus.isReverted());
        assertFalse(transactionStatus.isFatal());
    }

    @Test
    public void testFatalStatatus() {
        TransactionStatus transactionStatus = TransactionStatus.fatal("1");
        assertFalse(transactionStatus.isSuccess());
        assertFalse(transactionStatus.isRejected());
        assertFalse(transactionStatus.isFailed());
        assertFalse(transactionStatus.isReverted());
        assertTrue(transactionStatus.isFatal());
    }

    @Test(expected = NullPointerException.class)
    public void testRejectedStatusWithNullCause() {
        TransactionStatus.rejection(null);
    }

    @Test(expected =  NullPointerException.class)
    public void testNonRevertedFailureStatusWithNullCause() {
        TransactionStatus.nonRevertedFailure(null);
    }

    @Test
    public void testSuccessRevertAndFatalCausesAreNonNull() {
        assertNotNull(TransactionStatus.successful().causeOfError);
        assertNotNull(TransactionStatus.revertedFailure().causeOfError);
        assertNotNull(TransactionStatus.fatal("1").causeOfError);
    }

    @Test
    public void testEqualityOfDifferentRejections() {
        TransactionStatus rejected1 = TransactionStatus.rejection("1");
        TransactionStatus rejected2 = TransactionStatus.rejection("2");
        assertNotEquals(rejected1, rejected2);
    }

    @Test
    public void testEqualityOfDifferentFailures() {
        TransactionStatus rejected1 = TransactionStatus.nonRevertedFailure("1");
        TransactionStatus rejected2 = TransactionStatus.nonRevertedFailure("2");
        assertNotEquals(rejected1, rejected2);
    }

    @Test
    public void testEquality() {
        TransactionStatus success = TransactionStatus.successful();
        TransactionStatus rejected = TransactionStatus.rejection("rejected");
        TransactionStatus failed = TransactionStatus.nonRevertedFailure("failed");
        TransactionStatus reverted = TransactionStatus.revertedFailure();
        TransactionStatus fatal = TransactionStatus.fatal("fatal");

        // First verify each status is equal to an equivalent self.
        assertEquals(success, TransactionStatus.successful());
        assertEquals(rejected, TransactionStatus.rejection("rejected"));
        assertEquals(failed, TransactionStatus.nonRevertedFailure("failed"));
        assertEquals(reverted, TransactionStatus.revertedFailure());
        assertEquals(fatal, TransactionStatus.fatal("fatal"));

        // Now verify none of these 5 statuses are equal to each other.
        assertNotEquals(success, rejected);
        assertNotEquals(success, failed);
        assertNotEquals(success, reverted);
        assertNotEquals(success, fatal);

        assertNotEquals(rejected, failed);
        assertNotEquals(rejected, reverted);
        assertNotEquals(rejected, fatal);

        assertNotEquals(failed, reverted);
        assertNotEquals(failed, fatal);

        assertNotEquals(reverted, fatal);
    }
}
