package com.ufersa.CodePublish.components.publication.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class RatingId implements Serializable {
    private Long publicationId;
    private Long userId;
}
