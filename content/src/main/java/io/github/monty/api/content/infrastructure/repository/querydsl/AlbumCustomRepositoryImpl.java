package io.github.monty.api.content.infrastructure.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.monty.api.content.domain.model.aggregate.Album;
import io.github.monty.api.content.domain.model.entity.Tag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.github.monty.api.content.domain.model.aggregate.QAlbum.album;

@Repository
public class AlbumCustomRepositoryImpl extends QuerydslRepositorySupport implements AlbumCustomRepository {

    private final JPAQueryFactory queryFactory;

    public AlbumCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Album.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Album> findByTagList(List<Tag> tagList) {
        return queryFactory
                .selectFrom(album)
                .where(
                        album.tags
                                .any()
                                .in(tagList)
                )
                .fetch();
    }
}
