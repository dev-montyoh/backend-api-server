package dev.montyoh.content.infrastructure.repository.jpa;

import dev.montyoh.content.domain.model.aggregate.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumJpaRepository extends JpaRepository<Album, Long> {

}
