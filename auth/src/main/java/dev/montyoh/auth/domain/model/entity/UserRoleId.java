package dev.montyoh.auth.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleId implements Serializable {

    @Column(name = "user_no")
    private String userNo;

    @Column(name = "role_id")
    private Long roleId;
}
