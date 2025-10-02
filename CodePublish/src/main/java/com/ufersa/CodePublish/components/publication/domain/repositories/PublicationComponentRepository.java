package com.ufersa.CodePublish.components.publication.domain.repositories;

import com.ufersa.CodePublish.components.publication.domain.entities.PublicationComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationComponentRepository extends JpaRepository<PublicationComponent, Long> {
}
