package dev.montyoh.content.infrastructure.repository;

import dev.montyoh.content.domain.model.aggregate.Album;
import dev.montyoh.content.domain.model.entity.Tag;
import dev.montyoh.content.domain.repository.AlbumRepository;
import dev.montyoh.content.infrastructure.repository.jpa.AlbumJpaRepository;
import dev.montyoh.content.infrastructure.repository.querydsl.AlbumCustomRepository;
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
