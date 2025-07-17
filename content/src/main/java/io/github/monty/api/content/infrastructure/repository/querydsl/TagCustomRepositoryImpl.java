package io.github.monty.api.content.infrastructure.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.github.monty.api.content.domain.model.entity.Tag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.github.monty.api.content.domain.model.entity.QTag.tag;

@Repository
public class TagCustomRepositoryImpl extends QuerydslRepositorySupport implements TagCustomRepository {

    private final JPAQueryFactory queryFactory;

    public TagCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Tag.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Tag> findTagListByTagNameList(List<String> tagList) {
        return queryFactory.selectFrom(tag)
                .where(tag.tagName.in(tagList))
                .fetch();
    }
}
