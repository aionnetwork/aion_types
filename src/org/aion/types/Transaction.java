package org.aion.types;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import org.aion.types.internal_util.ByteUtil;

/**
 * Represents an "external" transaction on the Aion Network.
 * An external transaction is a tx whose sender is NOT a contract.
 *
 * This class is immutable,
 */
public final class Transaction {

    public final AionAddress senderAddress;
    public final AionAddress destinationAddress;
    private final byte[] transactionHash;
    public final BigInteger value;
    public final BigInteger nonce;
    public final long energyPrice;
    public final long energyLimit;
    public final boolean isCreate;
    private final byte[] transactionData;

    public Transaction(AionAddress senderAddress
        , AionAddress destinationAddress
        , byte[] transactionHash
        , BigInteger value
        , BigInteger nonce
        , long energyPrice
        , long energyLimit
        , byte[] transactionData
    ) {
        if (null == senderAddress) {
            throw new NullPointerException("No sender");
        }
        if (null == transactionHash) {
            throw new NullPointerException("No transaction hash");
        }
        if (null == value) {
            throw new NullPointerException("No value");
        }
        if (null == nonce) {
            throw new NullPointerException("No nonce");
        }
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative value");
        }
        if (nonce.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Negative nonce");
        }
        if (energyPrice < 0) {
            throw new IllegalArgumentException("Negative energy price");
        }
        if (energyLimit < 0) {
            throw new IllegalArgumentException("Negative energy limit");
        }
        if (null == transactionData) {
            throw new NullPointerException("Null data");
        }

        this.senderAddress = senderAddress;
        this.destinationAddress = destinationAddress;
        this.transactionHash = new byte[transactionHash.length];
        System.arraycopy(transactionHash, 0, this.transactionHash, 0, transactionHash.length);
        this.value = value;
        this.nonce = nonce;
        this.energyPrice = energyPrice;
        this.energyLimit = energyLimit;
        this.isCreate = (destinationAddress == null);
        this.transactionData = new byte[transactionData.length];
        System.arraycopy(transactionData, 0, this.transactionData, 0, transactionData.length);
    }

    public byte[] copyOfTransactionHash() {
        byte[] transactionHashCopy = new byte[transactionHash.length];
        System.arraycopy(transactionHash, 0, transactionHashCopy, 0, transactionHash.length);
        return transactionHashCopy;
    }

    public byte[] copyOfTransactionData() {
        byte[] transactionDataCopy = new byte[transactionData.length];
        System.arraycopy(transactionData, 0, transactionDataCopy, 0, transactionData.length);
        return transactionDataCopy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Transaction)) {
            return false;
        } else {
            Transaction otherObject = (Transaction) obj;
            return this.senderAddress.equals(otherObject.senderAddress)
                    && this.destinationAddress.equals(otherObject.destinationAddress)
                    && this.value.equals(otherObject.value)
                    && this.nonce.equals(otherObject.nonce)
                    && this.energyPrice == otherObject.energyPrice
                    && this.energyLimit == otherObject.energyLimit
                    && this.isCreate == otherObject.isCreate
                    && Arrays.equals(this.transactionHash, otherObject.transactionHash)
                    && Arrays.equals(this.transactionData, otherObject.transactionData);
        }
    }

    @Override
    public String toString() {
        String dataString = ByteUtil.bytesToString(transactionData);
        return "TransactionData ["
            + "hash="
            + ByteUtil.bytesToString(transactionHash)
            + ", nonce="
            + nonce
            + ", destinationAddress="
            + destinationAddress
            + ", value="
            + value
            + ", data="
            + dataString
            + ", energyLimit="
            + this.energyLimit
            + ", energyPrice="
            + this.energyPrice
            + "]";
    }

    @Override
    public int hashCode() {
        int result = Objects
            .hash(senderAddress, destinationAddress, value, nonce, energyPrice, energyLimit,
                isCreate);
        result = 31 * result + Arrays.hashCode(transactionHash);
        result = 31 * result + Arrays.hashCode(transactionData);
        return result;
    }
}
