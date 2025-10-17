package io.github.monty.api.user.domain.model.entity;

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
public class MemberRoleId implements Serializable {

    @Column(name = "member_no")
    private String memberNo;

    @Column(name = "role_id")
    private int roleId;
}
