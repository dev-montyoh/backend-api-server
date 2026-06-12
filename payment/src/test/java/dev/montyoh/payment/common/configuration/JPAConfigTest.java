package dev.montyoh.payment.common.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JPAConfigTest {

    @Mock
    private EntityManager entityManager;

    @Test
    @DisplayName("JPAConfig.jpaQueryFactory() 는 JPAQueryFactory 를 반환한다.")
    void jpaQueryFactory_created() {
        JPAConfig jpaConfig = new JPAConfig(entityManager);
        JPAQueryFactory factory = jpaConfig.jpaQueryFactory();
        assertThat(factory).isNotNull();
    }
}
