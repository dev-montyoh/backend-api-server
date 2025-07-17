package io.github.monty.api.content.domain.service;

import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagFindServiceTest {

    @InjectMocks
    private TagFindService tagFindService;

    @Mock
    private TagRepository tagRepository;

    @Test
    @DisplayName("태그를 조회한다.")
    void findTagList() {
        //  given
        String tagName1 = "tag1";
        String tagName2 = "tag2";
        Tag tag1 = Tag.builder()
                .tagId(0L)
                .tagName(tagName1)
                .build();
        Tag tag2 = Tag.builder()
                .tagId(1L)
                .tagName(tagName2)
                .build();
        given(tagRepository.findTagListByTagNameList(anyList())).willReturn(List.of(tag1, tag2));

        //  when
        List<Tag> actual = tagFindService.findTagList(List.of(tagName1, tagName2));

        //  then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(tag1, tag2))
        );
    }
}