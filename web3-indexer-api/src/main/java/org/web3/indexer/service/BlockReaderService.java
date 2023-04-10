package org.web3.indexer.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3.entities.BlockEntity;
import org.web3.indexer.response.models.Block;
import org.web3.repositories.BlockRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BlockReaderService {
    private final BlockRepository blockRepository;

    public Optional<Block> getByHash(String blockHash) {
        Optional<BlockEntity> blockEntity  =
                blockRepository.findByBlockHash(blockHash);
        return blockEntity.map(this::buildBlock);
    }

    private Block buildBlock(BlockEntity blockEntity) {
        return Block.builder()
                .blockHash(blockEntity.getBlockHash())
                .status(blockEntity.getStatus().toString())
                .blockNumber(blockEntity.getBlockNumber())
                .timestamp(blockEntity.getTimestamp())
                .transactionsCount(blockEntity.getTransactionsCount())
                .build();
    }

}

