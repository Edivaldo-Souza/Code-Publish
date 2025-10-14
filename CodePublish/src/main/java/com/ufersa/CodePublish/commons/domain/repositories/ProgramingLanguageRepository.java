package com.ufersa.CodePublish.commons.domain.repositories;

import com.ufersa.CodePublish.commons.domain.entities.ProgramingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramingLanguageRepository extends JpaRepository<ProgramingLanguage, Long> {
    Boolean existsByName(String name);
}
