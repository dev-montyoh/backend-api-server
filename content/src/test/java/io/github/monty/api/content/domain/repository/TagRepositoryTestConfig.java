package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.infrastructure.repository.TagRepositoryImpl;
import io.github.monty.api.content.infrastructure.repository.jpa.TagJpaRepository;
import io.github.monty.api.content.infrastructure.repository.querydsl.TagCustomRepository;
import io.github.monty.api.content.infrastructure.repository.querydsl.TagCustomRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TagRepositoryTestConfig {
    @Bean
    public TagCustomRepository tagCustomRepository(JPAQueryFactory jpaQueryFactory) {
        return new TagCustomRepositoryImpl(jpaQueryFactory);
    }

    @Bean
    public TagRepository tagRepository(TagJpaRepository tagJpaRepository, TagCustomRepository tagCustomRepository) {
        return new TagRepositoryImpl(tagJpaRepository, tagCustomRepository);
    }
}
