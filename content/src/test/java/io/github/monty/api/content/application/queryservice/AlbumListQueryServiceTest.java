package io.github.monty.api.content.application.queryservice;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.vo.AlbumVo;
import io.github.monty.api.content.domain.model.vo.AlbumListVo;
import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.model.query.AlbumListQuery;
import io.github.monty.api.content.domain.service.AlbumFindService;
import io.github.monty.api.content.domain.service.TagFindService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AlbumListQueryServiceTest {

    @InjectMocks
    private  AlbumListQueryService albumListQueryService;

    @Mock
    private AlbumFindService albumFindService;

    @Mock
    private TagFindService tagFindService;

    @Test
    @DisplayName("앨범 리스트를 반환한다.")
    void getAlbumList() {
        //  given
        String tagName = "testTag";
        Tag tag = Tag.builder()
                .tagId(0L)
                .tagName(tagName)
                .build();
        given(tagFindService.findTagList(anyList())).willReturn(List.of(tag));

        Set<Tag> tags = new HashSet<>(){{
            add(tag);
        }};
        Album album = Album.builder().contentId(0L).tags(tags).build();
        List<AlbumVo> albumVoList = List.of(new AlbumVo(album));
        given(albumFindService.getAlbumList(anyList())).willReturn(albumVoList);

        AlbumListQuery albumListQuery = AlbumListQuery.builder().tagNameList(List.of(tagName)).build();

        //  when
        AlbumListVo actual = albumListQueryService.getAlbumList(albumListQuery);

        //  then
        assertAll(
                () -> assertThat(actual.albumVoList().get(0).contentId()).isEqualTo(album.getContentId())
        );
    }

}