package com.ufersa.CodePublish.components.publication.domain.repositories;

import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublicationRepository extends JpaRepository<Publication,Long> {

    @Query("""
       SELECT DISTINCT p FROM Publication p
       LEFT JOIN p.components c
       LEFT JOIN p.tags t
       LEFT JOIN p.programingLanguage pl
       LEFT JOIN p.category ct
       WHERE p.id>0 AND
       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')))
""")
    Page<Publication> findByText(String q, Pageable pageable);
}
