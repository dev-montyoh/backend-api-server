package io.github.monty.api.content.interfaces.rest.mapper;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.model.vo.AlbumListVo;
import io.github.monty.api.content.domain.model.vo.AlbumVo;
import io.github.monty.api.content.domain.model.query.AlbumListQuery;
import io.github.monty.api.content.interfaces.rest.dto.AlbumListRspDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AlbumListQueryMapperTest {
    private final AlbumListQueryMapper albumListQueryMapper = Mappers.getMapper(AlbumListQueryMapper.class);

    @Test
    void mapToQuery() {
        //  given
        String tagName = "testTag";

        //  when
        AlbumListQuery actual = albumListQueryMapper.mapToQuery(List.of(tagName));

        //  then
        assertAll(
                () -> assertThat(actual.getTagNameList().size()).isEqualTo(1),
                () -> assertThat(actual.getTagNameList().get(0)).isEqualTo(tagName)
        );
    }

    @Test
    void mapToRspDto() {
        //  given
        Tag tag = Tag.builder()
                .tagId(0L)
                .tagName("testTag")
                .build();
        Album album = Album.builder()
                .contentId(0L)
                .tags(Set.of(tag))
                .title("testAlbum").build();
        AlbumListVo albumListVo = new AlbumListVo(List.of(new AlbumVo(album)));

        //  when
        AlbumListRspDto actual = albumListQueryMapper.mapToRspDto(albumListVo);

        //  then
        assertAll(
                () -> assertThat(actual.albumList().size()).isEqualTo(1),
                () -> assertThat(actual.albumList().get(0).title()).isEqualTo(album.getTitle())
        );
    }
}