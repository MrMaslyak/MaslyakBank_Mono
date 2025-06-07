package MaslyakBank_Token.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_token")
public class UserTokenTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersTable user;

    @Column(name = "token")
    private String token;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "is_expired")
    private Boolean isExpired;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
