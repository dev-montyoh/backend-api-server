package dev.montyoh.content.infrastructure.repository.querydsl;

import dev.montyoh.content.domain.model.aggregate.Album;
import dev.montyoh.content.domain.model.entity.Tag;

import java.util.List;

public interface AlbumCustomRepository {

    /**
     * 선택된 태그 리스트로 선택된 앨범 리스트를 조회한다.
     *
     * @param tagList 태그 리스트
     * @return 앨범 조회 결과
     */
    List<Album> findByTagList(List<Tag> tagList);
}
