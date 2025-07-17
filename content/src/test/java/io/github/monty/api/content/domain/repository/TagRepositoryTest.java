package io.github.monty.api.content.domain.repository;

import io.github.monty.api.content.common.configuration.QueryDslConfig;
import io.github.monty.api.content.domain.model.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import({QueryDslConfig.class, TagRepositoryTestConfig.class})
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("태그 이름 리스트를 가지고 태그를 조회한다.")
    void findTagListByTagNameList() {
        //  given
        Tag tag1 = Tag.builder()
                .tagName("tag1")
                .build();
        Tag tag2 = Tag.builder()
                .tagName("tag2")
                .build();
        Tag tag3 = Tag.builder()
                .tagName("tag3")
                .build();
        tagRepository.saveAll(List.of(tag1, tag2, tag3));

        //  when
        List<Tag> tagList = tagRepository.findTagListByTagNameList(List.of("tag1", "tag2"));

        //  when
        assertAll(
                () -> assertThat(tagList).allSatisfy(tag -> assertThat(tag.getTagId()).isNotNull()),
                () -> assertThat(tagList).usingRecursiveComparison().isEqualTo(List.of(tag1, tag2))
        );
    }
}