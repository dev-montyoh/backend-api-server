package io.github.monty.api.content.infrastructure.repository.querydsl;

import io.github.monty.api.content.domain.model.entity.Tag;

import java.util.List;

public interface TagCustomRepository {

    List<Tag> findTagListByTagNameList(List<String> tagList);
}
