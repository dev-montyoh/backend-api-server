package io.github.monty.api.content.infrastructure.repository.jpa;

import io.github.monty.api.content.domain.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
}
