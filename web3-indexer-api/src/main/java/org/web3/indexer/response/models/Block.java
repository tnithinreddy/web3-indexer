package org.web3.indexer.response.models;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@AllArgsConstructor
@Getter
public class Block {
    String blockHash;
    BigInteger timestamp;
    int transactionsCount;
    BigInteger blockNumber;
    String status;
}
