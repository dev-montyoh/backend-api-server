package io.github.monty.api.content.domain.service;

import io.github.monty.api.content.domain.model.entity.Tag;
import io.github.monty.api.content.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagFindService {
    private final TagRepository tagRepository;

    /**
     * tagName 리스트를 가지고 태그를 조회한다.
     *
     * @param tagNameList   태그 이름 리스트
     * @return  태그 조회 결과
     */
    public List<Tag> findTagList(List<String> tagNameList) {
        return tagRepository.findTagListByTagNameList(tagNameList);
    }
}
