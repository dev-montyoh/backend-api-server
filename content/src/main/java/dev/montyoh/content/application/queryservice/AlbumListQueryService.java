package dev.montyoh.content.application.queryservice;

import dev.montyoh.content.domain.model.vo.AlbumListVo;
import dev.montyoh.content.domain.model.vo.AlbumVo;
import dev.montyoh.content.domain.model.entity.Tag;
import dev.montyoh.content.domain.model.query.AlbumListQuery;
import dev.montyoh.content.domain.service.AlbumFindService;
import dev.montyoh.content.domain.service.TagFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumListQueryService {
    private final AlbumFindService albumFindService;
    private final TagFindService tagFindService;

    /**
     * 앨범 리스트를 반환한다.
     *
     * @return 컨텐츠 리스트
     */
    public AlbumListVo getAlbumList(AlbumListQuery albumListQuery) {
        List<Tag> tagList = tagFindService.findTagList(albumListQuery.getTagNameList());
        List<AlbumVo> albumVoList = albumFindService.getAlbumList(tagList);
        return new AlbumListVo(albumVoList);
    }
}
