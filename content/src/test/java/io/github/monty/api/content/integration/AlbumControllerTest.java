package io.github.monty.api.content.integration;

import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.model.vo.AlbumVo;
import io.github.monty.api.content.interfaces.rest.constants.ContentApiUrl;
import io.github.monty.api.content.interfaces.rest.dto.AlbumListRspDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AlbumControllerTest extends WireMockServerTest {

    @Test
    @DisplayName("앨범 조회를 했고 성공 응답과 함께 데이터를 전달받는다.")
    void getAlbumList() {
        //  given
        Tag tag1 = Tag.builder()
                .tagName("tag1")
                .build();
        Tag tag2 = Tag.builder()
                .tagName("tag2")
                .build();
        this.insertTagList(List.of(tag1, tag2));
        Set<Tag> testSet1 = new HashSet<>() {{
            add(tag1);
            add(tag2);
        }};
        Album testAlbum1 = Album.builder()
                .imageUrl("testURL1")
                .title("testTitle1")
                .description("testDescription1")
                .tags(testSet1)
                .build();

        Set<Tag> testSet2 = new HashSet<>() {{
            add(tag2);
        }};
        Album testAlbum2 = Album.builder()
                .imageUrl("testURL2")
                .title("testTitle2")
                .description("testDescription2")
                .tags(testSet2)
                .build();
        List<Album> albums = this.insertAlbumList(List.of(testAlbum1, testAlbum2));

        //  when
        String requestUri = UriComponentsBuilder.fromPath(ContentApiUrl.CONTENT_V1_BASE_URL)
                .path(ContentApiUrl.ALBUM_LIST)
                .queryParam("tag", tag1.getTagName())
                .queryParam("tag", tag2.getTagName())
                .build()
                .toUriString();
        ResponseEntity<AlbumListRspDto> responseEntity = restTemplate.getForEntity(requestUri, AlbumListRspDto.class);
        Optional<AlbumListRspDto> actualOptional = Optional.ofNullable(responseEntity.getBody());
        AlbumListRspDto actual = actualOptional.orElse(
                AlbumListRspDto.builder()
                        .albumList(new ArrayList<>())
                        .build()
        );

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertNotNull(actual),
                () -> assertThat(actual.albumList().get(0).title()).isEqualTo(testAlbum1.getTitle()),
                () -> assertThat(actual.albumList().get(1).title()).isEqualTo(testAlbum2.getTitle())
        );
    }

    @Test
    @DisplayName("앨범 조회를 했고 성공 응답과 함께 데이터를 전달받는다. (전체 데이터)")
    void getAlbum_all() {
        //  given
        Tag tag1 = Tag.builder()
                .tagName("testTag_1")
                .build();
        Tag tag2 = Tag.builder()
                .tagName("testTag_2")
                .build();
        Tag tag3 = Tag.builder()
                .tagName("testTag_3")
                .build();
        this.insertTagList(List.of(tag1, tag2, tag3));
        Set<Tag> tagSet1 = new HashSet<>() {{
            add(tag1);
        }};
        Set<Tag> tagSet2 = new HashSet<>() {{
            add(tag2);
        }};
        Set<Tag> tagSet3 = new HashSet<>() {{
            add(tag3);
        }};

        Album album1 = Album.builder()
                .imageUrl("testURL1")
                .title("testTitle1")
                .description("testDescription1")
                .tags(tagSet1)
                .build();
        Album album2 = Album.builder()
                .imageUrl("testURL1")
                .title("testTitle1")
                .description("testDescription1")
                .tags(tagSet2)
                .build();
        Album album3 = Album.builder()
                .imageUrl("testURL1")
                .title("testTitle1")
                .description("testDescription1")
                .tags(tagSet3)
                .build();
        List<Album> albums = this.insertAlbumList(List.of(album1, album2, album3));
        List<AlbumVo> albumVoList = albums.stream()
                .map(AlbumVo::new)
                .toList();

        //  when
        String requestUri = UriComponentsBuilder.fromPath(ContentApiUrl.CONTENT_V1_BASE_URL)
                .path(ContentApiUrl.ALBUM_LIST)
                .build()
                .toUriString();
        ResponseEntity<AlbumListRspDto> responseEntity = restTemplate.getForEntity(requestUri, AlbumListRspDto.class);
        Optional<AlbumListRspDto> actualOptional = Optional.ofNullable(responseEntity.getBody());
        AlbumListRspDto actual = actualOptional.orElse(
                AlbumListRspDto.builder()
                        .albumList(new ArrayList<>())
                        .build()
        );

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertNotNull(actual),
                () -> assertThat(actual.albumList().get(0).title()).isEqualTo(albumVoList.get(0).title()),
                () -> assertThat(actual.albumList().get(1).title()).isEqualTo(albumVoList.get(1).title()),
                () -> assertThat(actual.albumList().get(2).title()).isEqualTo(albumVoList.get(2).title())
        );
    }

    @Test
    @DisplayName("앨범 조회를 했고 성공 응답과 함께 데이터를 전달받는다. (데이터 없음)")
    void getAlbum_noData() {
        //  given
        Tag tag1 = Tag.builder()
                .tagName("testTag_1")
                .build();
        this.insertTagList(List.of(tag1));

        //  when
        String requestUri = UriComponentsBuilder.fromPath(ContentApiUrl.CONTENT_V1_BASE_URL)
                .path(ContentApiUrl.ALBUM_LIST)
                .queryParam("tag", tag1.getTagName())
                .build()
                .toUriString();
        ResponseEntity<AlbumListRspDto> responseEntity = restTemplate.getForEntity(requestUri, AlbumListRspDto.class);
        Optional<AlbumListRspDto> actualOptional = Optional.ofNullable(responseEntity.getBody());
        AlbumListRspDto actual = actualOptional.orElse(
                AlbumListRspDto.builder()
                        .albumList(new ArrayList<>())
                        .build()
        );

        //  then
        assertAll(
                () -> assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertNotNull(actual),
                () -> assertThat(actual.albumList().size()).isEqualTo(0)
        );
    }
}
