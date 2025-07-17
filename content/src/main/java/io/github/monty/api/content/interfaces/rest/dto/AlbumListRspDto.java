package io.github.monty.api.content.interfaces.rest.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AlbumListRspDto (List<AlbumListRspDtoItem> albumList){
}
