package dev.montyoh.content.domain.service;

import dev.montyoh.content.domain.model.aggregate.Album;
import dev.montyoh.content.domain.model.vo.AlbumVo;
import dev.montyoh.content.domain.model.entity.Tag;
import dev.montyoh.content.domain.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumFindService {
    private final AlbumRepository albumRepository;

    /**
     * 앨범 리스트를 반환한다.
     *
     * @return 컨텐츠 리스트
     */
    public List<AlbumVo> getAlbumList(List<Tag> tagList) {
        List<Album> albumList = tagList.isEmpty()
                ? albumRepository.findAll()
                : albumRepository.findByTagList(tagList);
        return albumList.stream()
                .map(AlbumVo::new)
                .toList();
    }
}
