package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;

import java.util.List;

public interface AlbumRepository {

    List<Album> findAll();

    List<Album> findByTagList(List<Tag> tags);

    Album save(Album album);

    List<Album> saveAll(List<Album> albums);
}
