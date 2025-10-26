package com.ufersa.CodePublish.components.publication.domain.repositories;

import com.ufersa.CodePublish.components.publication.domain.entities.PublicationUserRating;
import com.ufersa.CodePublish.components.publication.domain.entities.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationUserRatingRepository extends JpaRepository<PublicationUserRating, RatingId> {
    Boolean existsByIdPublicationIdAndIdUserId(Long publicationId, Long userId);

    void deleteByIdPublicationId(Long publicationId);
}
