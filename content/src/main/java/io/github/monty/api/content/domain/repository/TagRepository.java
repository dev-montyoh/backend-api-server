package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.domain.model.entity.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findTagListByTagNameList(List<String> tagNameList);

    List<Tag> saveAll(List<Tag> tags);
}
