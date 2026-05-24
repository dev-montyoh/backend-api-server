package dev.montyoh.content.infrastructure.repository.jpa;

import dev.montyoh.content.domain.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
}
