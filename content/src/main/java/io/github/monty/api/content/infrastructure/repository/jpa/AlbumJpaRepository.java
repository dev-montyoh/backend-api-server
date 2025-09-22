package io.github.monty.api.content.infrastructure.repository.jpa;

import io.github.monty.api.content.domain.model.aggregate.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumJpaRepository extends JpaRepository<Album, Long> {

}
