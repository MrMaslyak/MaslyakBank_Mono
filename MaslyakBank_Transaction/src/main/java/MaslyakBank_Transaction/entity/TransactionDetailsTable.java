package MaslyakBank_Transaction.entity;



import entity.CardTable;
import enums.TransactionDirectionType;
import jakarta.persistence.*;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transaction_details")
@Data
public class TransactionDetailsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    @MapsId
    private TransactionTable transaction;

    @OneToOne
    @JoinColumn(name = "from_card_id", nullable = false)
    private CardTable fromAccountId;

    @OneToOne
    @JoinColumn(name = "to_card_id", nullable = false)
    private CardTable toAccountId;

    @Column(name = "direction_type", length = 10, nullable = false)
    private TransactionDirectionType directionType;

    @Column(name = "operation_at", nullable = false)
    private Date operationAt;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
