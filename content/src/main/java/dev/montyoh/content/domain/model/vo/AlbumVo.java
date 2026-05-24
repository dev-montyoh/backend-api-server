package dev.montyoh.content.domain.model.vo;

import dev.montyoh.content.domain.model.aggregate.Album;

import java.util.List;

public record AlbumVo(long contentId, String imageUrl, String title, String description, List<TagVo> tagVoList) {

    public AlbumVo(Album album) {
        this(
                album.getContentId(),
                album.getImageUrl(),
                album.getTitle(),
                album.getDescription(),
                album.getTags()
                        .stream().map(TagVo::new)
                        .toList()
        );
    }
}
