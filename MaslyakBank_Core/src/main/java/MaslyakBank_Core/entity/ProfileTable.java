package MaslyakBank_Core.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Data
public class ProfileTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id")
    @OneToOne(cascade = CascadeType.ALL)
    private UUID user_id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "city")
    private String city;

    @Column(name = "avatar_url")
    private String avatar_url;

    @Column(name = "bio")
    private String bio;

    @Column(name = "birth_day")
    private Date birth_day;

    @Column(name = "is_completed")
    private boolean is_completed;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;

}
