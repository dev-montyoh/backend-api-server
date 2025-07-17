package io.github.monty.api.content.domain.model.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "content_album")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Album extends Content {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
