package dev.montyoh.content.infrastructure.repository.querydsl;

import dev.montyoh.content.domain.model.entity.Tag;

import java.util.List;

public interface TagCustomRepository {

    List<Tag> findTagListByTagNameList(List<String> tagList);
}
