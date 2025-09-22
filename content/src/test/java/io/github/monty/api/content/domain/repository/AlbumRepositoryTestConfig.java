package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.infrastructure.repository.AlbumRepositoryImpl;
import io.github.monty.api.content.infrastructure.repository.jpa.AlbumJpaRepository;
import io.github.monty.api.content.infrastructure.repository.querydsl.AlbumCustomRepository;
import io.github.monty.api.content.infrastructure.repository.querydsl.AlbumCustomRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AlbumRepositoryTestConfig {
    @Bean
    public AlbumCustomRepository albumCustomRepository(JPAQueryFactory jpaQueryFactory) {
        return new AlbumCustomRepositoryImpl(jpaQueryFactory);
    }

    @Bean
    public AlbumRepository albumRepository(AlbumJpaRepository albumJpaRepository, AlbumCustomRepository albumCustomRepository) {
        return new AlbumRepositoryImpl(albumJpaRepository, albumCustomRepository);
    }
}
