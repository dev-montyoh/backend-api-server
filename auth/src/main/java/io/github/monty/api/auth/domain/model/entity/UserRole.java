package io.github.monty.api.auth.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @EmbeddedId
    private UserRoleId userRoleId;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
}
