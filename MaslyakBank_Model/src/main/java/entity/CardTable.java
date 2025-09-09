package entity;



import enums.CardType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
public class CardTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountTable account;

    @Column(name = "card_number", length = 19, unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "is_expired", nullable = false)
    private boolean is_expired;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Column(name = "card_type",  nullable = false)
    private CardType cardType;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
