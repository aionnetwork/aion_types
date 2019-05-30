package org.aion.aion_types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LogTest {
    private byte[] address;
    private byte[] data;

    @Before
    public void setup() {
        this.address = randomBytes(32);
        this.data = randomBytes(32);
        System.out.println("Test is using random address: " + bytesToString(this.address));
        System.out.println("Test is using random data: " + bytesToString(this.data));
    }

    @After
    public void tearDown() {
        this.address = null;
        this.data = null;
    }

    @Test
    public void testDataOnlyLog() {
        Log log = Log.dataOnly(this.address, this.data);
        assertArrayEquals(this.address, log.copyOfAddress());
        assertArrayEquals(this.data, log.copyOfData());
    }

    @Test
    public void testLogWithEmptyTopics() {
        Log log = Log.topicsAndData(this.address, Collections.emptyList(), this.data);
        assertArrayEquals(this.address, log.copyOfAddress());
        assertArrayEquals(this.data, log.copyOfData());
        assertEquals(Collections.emptyList(), log.copyOfTopics());
    }

    @Test
    public void testLogWithSingleTopic() {
        byte[] topic = randomBytes(10);
        System.out.println("Test is using random topic: " + bytesToString(topic));

        Log log = Log.topicsAndData(this.address, Collections.singletonList(topic), this.data);
        assertArrayEquals(this.address, log.copyOfAddress());
        assertArrayEquals(this.data, log.copyOfData());
        assertListsEqual(Collections.singletonList(topic), log.copyOfTopics());
    }

    @Test
    public void testLogWithMultipleTopics() {
        byte[] topic1 = randomBytes(10);
        byte[] topic2 = randomBytes(25);
        byte[] topic3 = randomBytes(1);
        System.out.println("Test is using random topics: " + bytesToString(topic1) + " " + bytesToString(topic2) + " " + bytesToString(topic3));

        List<byte[]> topics = new ArrayList<>();
        topics.add(topic1);
        topics.add(topic2);
        topics.add(topic3);

        Log log = Log.topicsAndData(this.address, topics, this.data);
        assertArrayEquals(this.address, log.copyOfAddress());
        assertArrayEquals(this.data, log.copyOfData());
        assertListsEqual(topics, log.copyOfTopics());
    }

    @Test
    public void testLogWithAllEmptyByteArrays() {
        Log log = Log.topicsAndData(new byte[0], Collections.singletonList(new byte[0]), new byte[0]);
        assertArrayEquals(new byte[0], log.copyOfAddress());
        assertArrayEquals(new byte[0], log.copyOfData());
        assertListsEqual(Collections.singletonList(new byte[0]), log.copyOfTopics());
    }

    @Test
    public void testImmutabilityOfInputs() {
        byte[] topic = randomBytes(20);
        System.out.println("Test is using random topic: " + bytesToString(topic));

        // Save copies of the original arrays.
        byte[] originalAddress = copyOf(this.address);
        byte[] originalData = copyOf(this.data);
        byte[] originalTopic = copyOf(topic);

        List<byte[]> actualTopicList = new ArrayList<>();
        actualTopicList.add(topic);

        // Test out all the possible ways logs can be constructed.
        Log dataOnlyLog = Log.dataOnly(this.address, this.data);
        Log topicsAndDataLog = Log.topicsAndData(this.address, actualTopicList, this.data);

        // Mutate the inputs.
        this.address[0] = (byte) ~this.address[0];
        this.data[0] = (byte) ~this.data[0];
        topic[0] = (byte) ~topic[0];
        actualTopicList.clear();

        // Verify neither of the two logs have been mutated.
        assertArrayEquals(originalAddress, dataOnlyLog.copyOfAddress());
        assertArrayEquals(originalData, dataOnlyLog.copyOfData());
        assertTrue(dataOnlyLog.copyOfTopics().isEmpty());

        assertArrayEquals(originalAddress, topicsAndDataLog.copyOfAddress());
        assertArrayEquals(originalData, topicsAndDataLog.copyOfData());
        assertArrayEquals(originalTopic, topicsAndDataLog.copyOfTopics().get(0));
    }

    @Test
    public void testImmutabilityOfReturnedFields() {
        byte[] topic = randomBytes(20);
        System.out.println("Test is using random topic: " + bytesToString(topic));

        // Test out all the possible ways logs can be constructed.
        Log dataOnlyLog = Log.dataOnly(this.address, this.data);
        Log topicsAndDataLog = Log.topicsAndData(this.address, Collections.singletonList(topic), this.data);

        // Save copies of the original arrays.
        byte[] originalAddress = copyOf(this.address);
        byte[] originalData = copyOf(this.data);
        byte[] originalTopic = copyOf(topic);
        List<byte[]> originalTopicList = new ArrayList<>();
        originalTopicList.add(originalTopic);

        // Grab the returned fields.
        byte[] returnedAddressFromDataOnlyLog = dataOnlyLog.copyOfAddress();
        byte[] returnedDataFromDataOnlyLog = dataOnlyLog.copyOfData();

        byte[] returnedAddressFromTopicsAndDataLog = topicsAndDataLog.copyOfAddress();
        byte[] returnedDataFromTopicsAndDataLog = topicsAndDataLog.copyOfData();
        byte[] returnedTopicFromTopicsAndDataLog = topicsAndDataLog.copyOfTopics().get(0);
        List<byte[]> returnedTopicListFromTopicsAndDataLog = topicsAndDataLog.copyOfTopics();

        // Mutate returned fields.
        returnedAddressFromDataOnlyLog[0] = (byte) ~returnedAddressFromDataOnlyLog[0];
        returnedDataFromDataOnlyLog[0] = (byte) ~returnedDataFromDataOnlyLog[0];

        returnedAddressFromTopicsAndDataLog[0] = (byte) ~returnedAddressFromTopicsAndDataLog[0];
        returnedDataFromTopicsAndDataLog[0] = (byte) ~returnedDataFromTopicsAndDataLog[0];
        returnedTopicFromTopicsAndDataLog[0] = (byte) ~returnedTopicFromTopicsAndDataLog[0];
        returnedTopicListFromTopicsAndDataLog.clear();

        // Verify neither of the two logs have been mutated.
        assertArrayEquals(originalAddress, dataOnlyLog.copyOfAddress());
        assertArrayEquals(originalData, dataOnlyLog.copyOfData());

        assertArrayEquals(originalAddress, topicsAndDataLog.copyOfAddress());
        assertArrayEquals(originalData, topicsAndDataLog.copyOfData());
        assertArrayEquals(originalTopic, topicsAndDataLog.copyOfTopics().get(0));
        assertListsEqual(originalTopicList, topicsAndDataLog.copyOfTopics());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAddress() {
        Log.topicsAndData(null, Collections.emptyList(), this.data);
    }

    @Test(expected = NullPointerException.class)
    public void testNullAddressForDataOnly() {
        Log.dataOnly(null, this.data);
    }

    @Test(expected = NullPointerException.class)
    public void testNullData() {
        Log.topicsAndData(this.address, Collections.emptyList(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDataForDataOnly() {
        Log.dataOnly(this.address, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTopics() {
        Log.topicsAndData(this.address, null, this.data);
    }

    @Test
    public void testTwoDataOnlyLogsEqual() {
        Log log1 = Log.dataOnly(this.address, this.data);
        Log log2 = Log.dataOnly(this.address, this.data);
        assertEquals(log1, log2);
    }

    @Test
    public void testLogsWithSameTopicsSameOrderEqual() {
        byte[] topic1 = randomBytes(10);
        byte[] topic2 = randomBytes(25);
        byte[] topic3 = randomBytes(1);
        System.out.println("Test is using random topics: " + bytesToString(topic1) + " " + bytesToString(topic2) + " " + bytesToString(topic3));

        List<byte[]> topics = new ArrayList<>();
        topics.add(topic1);
        topics.add(topic2);
        topics.add(topic3);

        Log log1 = Log.topicsAndData(this.address, topics, this.data);
        Log log2 = Log.topicsAndData(this.address, topics, this.data);
        assertEquals(log1, log2);
    }

    @Test
    public void testLogsWithSameTopicsDiffOrderEqual() {
        byte[] topic1 = randomBytes(10);
        byte[] topic2 = randomBytes(25);
        byte[] topic3 = randomBytes(1);
        System.out.println("Test is using random topics: " + bytesToString(topic1) + " " + bytesToString(topic2) + " " + bytesToString(topic3));

        List<byte[]> topics1 = new ArrayList<>();
        topics1.add(topic1);
        topics1.add(topic2);
        topics1.add(topic3);

        List<byte[]> topics2 = new ArrayList<>();
        topics2.add(topic1);
        topics2.add(topic3);
        topics2.add(topic2);

        Log log1 = Log.topicsAndData(this.address, topics1, this.data);
        Log log2 = Log.topicsAndData(this.address, topics2, this.data);
        assertNotEquals(log1, log2);
    }

    @Test
    public void testLogsWithAllEmptyFieldsEqual() {
        Log dataOnlyLog1 = Log.dataOnly(new byte[0], new byte[0]);
        Log dataOnlyLog2 = Log.dataOnly(new byte[0], new byte[0]);

        Log topicsAndDataOnlyLog1 = Log.topicsAndData(new byte[0], Collections.emptyList(), new byte[0]);
        Log topicsAndDataOnlyLog2 = Log.topicsAndData(new byte[0], Collections.emptyList(), new byte[0]);

        assertEquals(dataOnlyLog1, dataOnlyLog2);
        assertEquals(topicsAndDataOnlyLog1, topicsAndDataOnlyLog2);
        assertEquals(dataOnlyLog1, topicsAndDataOnlyLog1);
    }

    @Test
    public void testDataOnlyLogEqualToTopicsLogWithEmptyTopics() {
        Log dataOnlyLog = Log.dataOnly(this.address, this.data);
        Log topicsAndDataOnlyLog = Log.topicsAndData(this.address, Collections.emptyList(), this.data);

        assertEquals(dataOnlyLog, topicsAndDataOnlyLog);
    }

    @Test
    public void testDataOnlyLogUnequalToTopicsLogWithTopics() {
        byte[] topic = randomBytes(1);
        System.out.println("Test is using random topic: " + bytesToString(topic));

        Log dataOnlyLog = Log.dataOnly(this.address, this.data);
        Log topicsAndDataOnlyLog = Log.topicsAndData(this.address, Collections.singletonList(topic), this.data);

        assertNotEquals(dataOnlyLog, topicsAndDataOnlyLog);
    }

    private static byte[] copyOf(byte[] bytes) {
        return Arrays.copyOf(bytes, bytes.length);
    }

    private static void assertListsEqual(List<byte[]> list1, List<byte[]> list2) {
        assertEquals(list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            assertArrayEquals(list1.get(i), list2.get(i));
        }
    }

    private static byte[] randomBytes(int size) {
        byte[] randomBytes = new byte[size];
        new Random().nextBytes(randomBytes);
        return randomBytes;
    }

    private static String bytesToString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
