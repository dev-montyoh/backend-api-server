package dev.montyoh.content.domain.repository;

import dev.montyoh.content.domain.model.entity.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findTagListByTagNameList(List<String> tagNameList);

    List<Tag> saveAll(List<Tag> tags);
}
