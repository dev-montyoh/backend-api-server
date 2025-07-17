package io.github.monty.api.content.infrastructure.repository;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.repository.AlbumRepository;
import io.github.monty.api.content.infrastructure.repository.jpa.AlbumJpaRepository;
import io.github.monty.api.content.infrastructure.repository.querydsl.AlbumCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {
    private final AlbumJpaRepository albumJpaRepository;
    private final AlbumCustomRepository albumCustomRepository;

    @Override
    public List<Album> findAll() {
        return albumJpaRepository.findAll();
    }

    @Override
    public List<Album> findByTagList(List<Tag> tags) {
        return albumCustomRepository.findByTagList(tags);
    }

    @Override
    public Album save(Album album) {
        return albumJpaRepository.save(album);
    }

    @Override
    public List<Album> saveAll(List<Album> albums) {
        return albumJpaRepository.saveAll(albums);
    }
}
