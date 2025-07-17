package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.common.configuration.QueryDslConfig;
import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({QueryDslConfig.class, AlbumRepositoryTestConfig.class, TagRepositoryTestConfig.class})
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("선택된 태그 리스트로 앨범 리스트를 조회한다.")
    void findByTagList() {
        //  given
        Tag tag1 = Tag.builder()
                .tagName("tag1")
                .build();
        Tag tag2 = Tag.builder()
                .tagName("tag2")
                .build();
        tagRepository.saveAll(List.of(tag1, tag2));
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
        albumRepository.save(testAlbum1);

        //  when
        List<Album> actual = albumRepository.findByTagList(List.of(tag1, tag2));

        //  then
        assertAll(
                () -> assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(testAlbum1)
        );
    }
}