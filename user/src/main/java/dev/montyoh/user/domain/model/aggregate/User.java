package dev.montyoh.user.domain.model.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "USER_ID", nullable = false, length = 100)
    private String userId;

    @Column(name = "USER_NO", nullable = false, length = 100)
    private String userNo;

    @Column(name = "USER_LOGIN_ID", nullable = false, length = 100)
    private String loginId;

    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String password;
}
