package com.ufersa.CodePublish.components.publication.domain.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_publication_user_rating")
public class PublicationUserRating {
    @EmbeddedId
    private RatingId id;

    private Boolean positive;
}
