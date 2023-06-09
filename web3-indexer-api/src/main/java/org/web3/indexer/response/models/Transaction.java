package org.web3.indexer.response.models;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Transaction {
    String txnHash;
    String fromAddress;
    String toAddress;
    BigInteger txnValue;
    BigInteger gas;
    BigInteger gasPrice;
    BigInteger timestamp;
    String inputData;
    String blockHash;
}
