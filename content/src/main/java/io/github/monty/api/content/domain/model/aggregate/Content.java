package io.github.monty.api.content.domain.model.aggregate;

import io.github.monty.api.content.domain.model.entity.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "content", schema = "content")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Content {

    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @ManyToMany
    @JoinTable(
            name = "content_tag",
            schema = "content",
            joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id")
    )
    private Set<Tag> tags;
}
