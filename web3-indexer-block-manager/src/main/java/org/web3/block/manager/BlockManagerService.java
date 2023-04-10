package org.web3.block.manager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3.entities.BlockEntity;
import org.web3.indexer.node.NodeInterface;
import org.web3.repositories.BlockRepository;
import org.web3.transaction.retriever.BlockTransactionProcessor;
import org.web3j.protocol.core.methods.response.EthBlock;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BlockManagerService {

    private final BlockRepository blockRepository;

    private final NodeInterface nodeInterface;

    private final BlockTransactionProcessor blockTransactionProcessor;

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws IOException;
    }


    public EthBlock getBlock(long blockNumber) throws IOException {

        EthBlock block =  nodeInterface.ethGetBlockByNumber(BigInteger.valueOf(blockNumber), false);
//        List<EthBlock.TransactionObject> contractTransactions = getContractTransactions(block);
//        log.info("contractTransactions {}", contractTransactions.size());
//        Optional<TransactionReceipt> transReceipt = nodeInterface.ethGetTransactionReceipt("transactionHash");
//        Transaction transaction = ethTransaction.getResult();
//        int size = tokenImpl.getTransferEvents(transReceipt).size();
//        size = size -1;
//        BigInteger valueTransaction = tokenImpl.getTransferEvents(transReceipt).get(size).value;
        return block;
    }


    private void processBlock(EthBlock.Block block) {
        blockRepository.save(buildBlockEntity(block));
        // ideally we will not be publishing this message from code as this will not be transactional.
        // We will consume create/update events from the cdc of blocks table and publish to a kafka topic
        // not integrating with kafka currently invoking the message in a fire and forget mode
        publishBlockToTransactionManager(block);

    }

    private void publishBlockToTransactionManager(EthBlock.Block block) {
        CompletableFuture.runAsync(() -> {
            blockTransactionProcessor.process(block);
        });
    }

    private BlockEntity buildBlockEntity(EthBlock.Block block) {
        return BlockEntity.builder()
                .blockNumber(block.getNumber())
                .transactionsCount(block.getTransactions().size())
                .timestamp(block.getTimestamp())
                .blockHash(block.getHash())
                .build();
    }

    @PostConstruct
    public void subscribe() {
        Subscription subscription = (Subscription) nodeInterface.blockFlowable(false).subscribe(block -> {
            log.info(block);
            processBlock(block.getBlock());
        });
    }
}
