package io.github.monty.api.auth.domain.repository;

import io.github.monty.api.auth.domain.model.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({RoleRepositoryTestConfig.class})
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("해당 ID에 해동되는 모든 Role 을 조회한다.")
    void findAllByIdIn() {
        //  given
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

        //  when
        List<Role> actual = roleRepository.findAllByIdIn(List.of(role1.getId(), role2.getId()));

        //  then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(role1, role2))
        );
    }
}