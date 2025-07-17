package io.github.monty.api.content.domain.model.vo;

import io.github.monty.api.content.domain.model.entity.Tag;

public record TagVo(long tagId, String tagName) {
    public TagVo(Tag tag) {
        this(tag.getTagId(), tag.getTagName());
    }
}
