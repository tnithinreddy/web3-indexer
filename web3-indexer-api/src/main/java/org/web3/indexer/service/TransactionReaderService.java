package org.web3.indexer.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3.entities.TransactionEntity;
import org.web3.indexer.response.models.Transaction;
import org.web3.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TransactionReaderService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsOfAddress(String address) {
        List<TransactionEntity> transactionEntities =
                transactionRepository.findByFromAddressOrToAddress(address, address);
        return transactionEntities.stream().map(transactionEntity -> buildTransaction(transactionEntity)).collect(Collectors.toList());
    }

    private Transaction buildTransaction(TransactionEntity transactionEntity) {
        return Transaction.builder()
                .blockHash(transactionEntity.getBlockHash())
                .fromAddress(transactionEntity.getFromAddress())
                .gas(transactionEntity.getGas())
                .gasPrice(transactionEntity.getGasPrice())
                .inputData(transactionEntity.getInputData())
                .timestamp(transactionEntity.getTimestamp())
                .toAddress(transactionEntity.getToAddress())
                .txnHash(transactionEntity.getTxnHash())
                .build();
    }

}
