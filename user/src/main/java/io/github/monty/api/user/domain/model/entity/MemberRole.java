package io.github.monty.api.user.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_role", schema = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRole {
    @EmbeddedId
    private MemberRoleId memberRoleId;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;
}
