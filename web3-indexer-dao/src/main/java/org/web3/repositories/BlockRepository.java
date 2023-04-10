package org.web3.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.web3.entities.BlockEntity;

@Repository
public interface BlockRepository extends CrudRepository<BlockEntity, String> { }