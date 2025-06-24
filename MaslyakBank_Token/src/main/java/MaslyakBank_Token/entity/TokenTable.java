package MaslyakBank_Token.entity;


import entity.UsersTable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_token")
public class TokenTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersTable user;

    @Column(name = "token")
    private String token;

    @Column(name = "is_valid")
    private boolean isValid;

    @Column(name = "is_expired")
    private boolean isExpired;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
