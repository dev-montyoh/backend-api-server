package io.github.monty.api.content.domain.model.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlbumListQuery {
    private List<String> tagNameList;
}
