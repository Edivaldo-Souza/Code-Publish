package com.ufersa.CodePublish.commons.domain.repositories;

import com.ufersa.CodePublish.commons.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
