package com.ufersa.CodePublish.components.files.domain.repositories;

import com.ufersa.CodePublish.components.files.domain.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByUrl(String url);
}
