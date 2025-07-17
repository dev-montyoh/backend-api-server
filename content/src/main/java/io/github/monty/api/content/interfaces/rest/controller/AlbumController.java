package io.github.monty.api.content.interfaces.rest.controller;

import io.github.monty.api.content.application.queryservice.AlbumListQueryService;
import io.github.monty.api.content.domain.model.vo.AlbumListVo;
import io.github.monty.api.content.domain.model.query.AlbumListQuery;
import io.github.monty.api.content.interfaces.rest.constants.ContentApiUrl;
import io.github.monty.api.content.interfaces.rest.dto.AlbumListRspDto;
import io.github.monty.api.content.interfaces.rest.mapper.AlbumListQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ContentApiUrl.CONTENT_V1_BASE_URL)
public class AlbumController {
    private final AlbumListQueryService albumListQueryService;
    private final AlbumListQueryMapper albumListQueryMapper;

    /**
     * 앨범 컨텐츠 리스트 조회
     *
     * @param tagList 조회하고 싶은 Tag
     * @return 조회 결과
     */
    @GetMapping(ContentApiUrl.ALBUM_LIST)
    public ResponseEntity<AlbumListRspDto> getAlbumList(@RequestParam(value = "tag", required = false) List<String> tagList) {
        AlbumListQuery albumListQuery = albumListQueryMapper.mapToQuery(tagList);
        AlbumListVo albumListVo = albumListQueryService.getAlbumList(albumListQuery);
        AlbumListRspDto albumListRspDto = albumListQueryMapper.mapToRspDto(albumListVo);
        return new ResponseEntity<>(albumListRspDto, HttpStatus.OK);
    }
}
