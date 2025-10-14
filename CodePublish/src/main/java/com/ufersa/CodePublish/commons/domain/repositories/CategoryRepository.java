package com.ufersa.CodePublish.commons.domain.repositories;

import com.ufersa.CodePublish.commons.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);
}
