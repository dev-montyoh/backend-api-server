package dev.montyoh.content.domain.model.vo;

import dev.montyoh.content.domain.model.entity.Tag;

public record TagVo(long tagId, String tagName) {
    public TagVo(Tag tag) {
        this(tag.getTagId(), tag.getTagName());
    }
}
