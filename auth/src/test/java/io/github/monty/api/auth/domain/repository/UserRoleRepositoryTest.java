package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.domain.model.entity.Role;
import io.github.monty.api.auth.domain.model.entity.UserRole;
import io.github.monty.api.auth.domain.model.entity.UserRoleId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({UserRoleRepositoryTestConfig.class, RoleRepositoryTestConfig.class})
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("UserRole 을 userNo 를 통해서 조회한다.")
    void findByUserRoleIdUserNo() {
        //  given
        String userNo = "userNo";
        Role role1 = Role.builder()
                .name("TEST_ROLE_1")
                .description("TEST_ROLE_1_DESCRIPTION")
                .build();
        Role role2 = Role.builder()
                .name("TEST_ROLE_2")
                .description("TEST_ROLE_2_DESCRIPTION")
                .build();
        Role role3 = Role.builder()
                .name("TEST_ROLE_3")
                .description("TEST_ROLE_3_DESCRIPTION")
                .build();
        roleRepository.saveAll(List.of(role1, role2, role3));

        UserRoleId userRoleId1 = UserRoleId.builder()
                .userNo(userNo)
                .roleId(role1.getId())
                .build();
        UserRole userRole1 = UserRole.builder()
                .userRoleId(userRoleId1)
                .role(role1)
                .build();

        UserRoleId userRoleId2 = UserRoleId.builder()
                .userNo(userNo)
                .roleId(role2.getId())
                .build();
        UserRole userRole2 = UserRole.builder()
                .userRoleId(userRoleId2)
                .role(role2)
                .build();

        UserRoleId userRoleId3 = UserRoleId.builder()
                .userNo("anotherUserNo")
                .roleId(role3.getId())
                .build();
        UserRole.builder()
                .userRoleId(userRoleId3)
                .role(role3)
                .build();

        userRoleRepository.saveAll(List.of(userRole1, userRole2));

        //  when
        List<UserRole> actual = userRoleRepository.findByUserRoleIdUserNo(userNo);

        //  then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(userRole1, userRole2))
        );
    }
}