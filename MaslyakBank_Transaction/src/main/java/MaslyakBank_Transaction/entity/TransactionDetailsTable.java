package MaslyakBank_Transaction.entity;



import MaslyakBank_Transaction.enums.TransactionDirectionType;
import entity.AccountTable;
import entity.CardTable;
import jakarta.persistence.*;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transaction_details")
@Data
public class TransactionDetailsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransactionTable transaction;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountTable accountTable;

    @OneToOne
    @JoinColumn(name = "card_id", nullable = false)
    private CardTable cardTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction_type", length = 10, nullable = false)
    private TransactionDirectionType directionType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;


    @Column(name = "created_at", nullable = false)
    private Date createdAt;


}
