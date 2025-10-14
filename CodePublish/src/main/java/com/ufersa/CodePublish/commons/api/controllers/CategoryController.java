package com.ufersa.CodePublish.commons.api.controllers;

import com.ufersa.CodePublish.commons.ApiResponse;
import com.ufersa.CodePublish.commons.ResponseUtil;
import com.ufersa.CodePublish.commons.api.dtos.CategoryDto;
import com.ufersa.CodePublish.commons.domain.entities.Category;
import com.ufersa.CodePublish.commons.domain.repositories.CategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAll(HttpServletRequest request) {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDtos.add(categoryDto);
        }

        ApiResponse<List<CategoryDto>> response = ResponseUtil
                .success(categoryDtos,"Categorias encontradas",request.getRequestURI());

        return ResponseEntity.ok(response);
    }
}
