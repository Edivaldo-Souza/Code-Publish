package com.ufersa.CodePublish.components.publication.domain.repositories;

import com.ufersa.CodePublish.components.publication.domain.entities.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication,Long> {

//    @Query("""
//       SELECT DISTINCT p FROM Publication p
//       LEFT JOIN p.components c
//       LEFT JOIN p.tags t
//       LEFT JOIN p.programingLanguage pl
//       LEFT JOIN p.category ct
//       WHERE p.id > :lastId AND
//       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
//       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
//       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
//       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
//       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%'))) OR
//       LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
//       ORDER BY p.id ASC
//""")
//    Page<Publication> findByTextWithBiggerId(String q, @Param("lastId") Long lastId,  Pageable pageable);

    @Query("""
       SELECT DISTINCT p FROM Publication p
       LEFT JOIN p.components c
       LEFT JOIN p.tags t
       LEFT JOIN p.programingLanguage pl
       LEFT JOIN p.category ct
       WHERE
       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%'))) OR 
       LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
       ORDER BY p.id ASC
""")
    Page<Publication> findByText(String q, Pageable pageable);

    @Query("""
       SELECT DISTINCT p FROM Publication p
       LEFT JOIN p.components c
       LEFT JOIN p.tags t
       LEFT JOIN p.programingLanguage pl
       LEFT JOIN p.category ct
       WHERE p.user.id = :userId AND
       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%')))
       ORDER BY p.id ASC
""")
    Page<Publication> findByTextAndUser(String q, @Param("userId") Long userId, Pageable pageable);

    @Query("""
       SELECT DISTINCT p.id FROM Publication p
       LEFT JOIN p.components c
       LEFT JOIN p.tags t
       LEFT JOIN p.programingLanguage pl
       LEFT JOIN p.category ct
       WHERE p.user.id = :userId AND
       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%')))
""")
    List<Long> findIdsWithUser(String q, @Param("userId") Long userId);

    @Query("""
       SELECT DISTINCT p.id FROM Publication p
       LEFT JOIN p.components c
       LEFT JOIN p.tags t
       LEFT JOIN p.programingLanguage pl
       LEFT JOIN p.category ct
       WHERE
       (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(pl.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(ct.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
       LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%')))
""")
    List<Long> findIds(String q);

    @Query("""
       SELECT DISTINCT p FROM Publication p
       LEFT JOIN FETCH p.components c
       LEFT JOIN FETCH p.tags t
       LEFT JOIN FETCH p.programingLanguage pl
       LEFT JOIN FETCH p.category ct
       WHERE p.id IN (:ids)
       ORDER BY p.upvotesAmount DESC
""")
    Page<Publication> findByTextAndUser(List<Long> ids, Pageable pageable);
}
