package org.web3.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "transactions")
@Data
public class TransactionEntity extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    String txnHash;

    @Column(nullable = false)
    String fromAddress;

    @Column(nullable = true)
    String toAddress;

    @Column(nullable = false)
    BigInteger txnValue;

    @Column(nullable = false)
    BigInteger gas;

    @Column(nullable = false)
    BigInteger gasPrice;

    @Column(nullable = false)
    BigInteger timestamp;

    String inputData;

    @Column(nullable = false)
    String blockHash;

}
