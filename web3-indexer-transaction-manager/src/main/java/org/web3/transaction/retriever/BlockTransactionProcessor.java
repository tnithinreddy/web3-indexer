package org.web3.transaction.retriever;

import io.github.resilience4j.retry.annotation.Retry;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3.self.heal.publisher.SelfHealMessagePayload;
import org.web3.self.heal.publisher.SelfHealService;
import org.web3.entities.TransactionEntity;
import org.web3.indexer.node.NodeInterface;
import org.web3.repositories.TransactionRepository;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetCode;

@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class BlockTransactionProcessor {
    // takes a block

    private final NodeInterface nodeInterface;

    private final TransactionRepository transactionRepository;

    private final SmartContractTransactionProcessor smartContractTransactionProcessor;

    private final SelfHealService selfHealService;

    public void persistTransactions(List<EthBlock.TransactionObject> transactions, BigInteger timestamp) {
        List<TransactionEntity> transactionEntities = transactions.stream()
                .map(transaction -> buildTransactionEntity(transaction, timestamp))
                .collect(Collectors.toList());
        transactionRepository.saveAll(transactionEntities);
    }

    private static TransactionEntity buildTransactionEntity(EthBlock.TransactionObject transaction, BigInteger timestamp) {
        return TransactionEntity.builder()
                .blockHash(transaction.getBlockHash())
                .fromAddress(transaction.getFrom())
                .txnHash(transaction.getHash())
                .toAddress(transaction.getTo())
                .inputData(transaction.getInput())
                .txnValue(transaction.getValue())
                .gas(transaction.getGas())
                .gasPrice(transaction.getGasPrice())
                .timestamp(timestamp)
                .build();

    }

    public List<EthBlock.TransactionObject> getSmartContractTransactions(List<EthBlock.TransactionResult> transactions) {
        // TODO: need to optimise this to send a batch request to determine if a an address belongs to smart contract
        // sending multiple ethGetCode requests slows down the processor
        return transactions.stream().filter(tx -> {
            EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();
            log.info(transaction.getFrom());
            try {
                return isContract(transaction.getTo());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).map(tx -> (EthBlock.TransactionObject) tx.get()).collect(Collectors.toList());
    }

    public boolean isContract(String address) throws IOException {
        EthGetCode ethGetCode = nodeInterface.ethGetCode(address, DefaultBlockParameterName.LATEST);
        if (ethGetCode.getCode().equals("0x")) {
            return false;
        }
        return true;
    }

    // this can be our kafka consumer - in this POC invoking this method from block manager

    private List<EthBlock.TransactionObject> getTransactions(EthBlock ethBlock) {
        return ethBlock.getBlock().getTransactions().stream()
                .map(txn -> (EthBlock.TransactionObject) txn.get()).collect(Collectors.toList());
    }

    @Retry(name = "BLOCK_TXN_PROCESSOR_RETRY_POLICY", fallbackMethod = "processBlockTransactionsFallback")
    private void processBlockTransactions(EthBlock.Block block) {
        try {
            EthBlock ethBlock = nodeInterface.ethGetBlockByNumber(block.getNumber(), true);
            List<EthBlock.TransactionObject> transactions = getTransactions(ethBlock);
            persistTransactions(transactions, ethBlock.getBlock().getTimestamp());
            List<EthBlock.TransactionObject> smartContractTransactions = getSmartContractTransactions(ethBlock.getBlock().getTransactions());
            publishToSmartContractProcessor(smartContractTransactions);
            publishTransactionsAckToBlockManager(block);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void publishTransactionsAckToBlockManager(EthBlock.Block block) {
        // send acknowledgement to block-manager,
        // without using kafka this will lead to circular dependency to avoiding it for now.
    }

    private void processBlockTransactionsFallback(EthBlock.Block block, RuntimeException re) {
        log.error("error processing block with hash {}, exception {}", block.getHash(), re);
        SelfHealMessagePayload selfHealMessagePayload = SelfHealMessagePayload.
                builder()
                .topic("pending-blocks-topic")
                .identifier(block.getHash())
                .payload(block)
                .build();
        selfHealService.publish(selfHealMessagePayload);
    }

    // TODO: publish to kafka topic
    private void publishToSmartContractProcessor(List<EthBlock.TransactionObject> smartContractTransactions) {
        smartContractTransactions.forEach(txn -> {
            CompletableFuture.runAsync(() -> {
                smartContractTransactionProcessor.process(txn);
            });
        });
    }

    public void process(EthBlock.Block block) {
        processBlockTransactions(block);
    }


}
