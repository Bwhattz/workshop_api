package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.model.Category;
import com.gabriel.workshop_api.request.CategoryRequest;
import com.gabriel.workshop_api.response.CategoryResponse;
import com.gabriel.workshop_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {

        Category category = categoryRequest.toEntity();

        Category serviceCategory = categoryService.save(category);

        CategoryResponse categoryResponse = CategoryResponse.fromCategory(serviceCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {

        List<CategoryResponse> listCategory = categoryService.findAll().stream()
                .map(CategoryResponse::fromCategory)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listCategory);
    }

    @PutMapping
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest) {

        Category updateCategoryEntity = categoryRequest.toEntity();

        Category serviceCategory = categoryService.update(updateCategoryEntity);

        CategoryResponse categoryResponse = CategoryResponse.fromCategory(serviceCategory);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryResponse);
    }

    @GetMapping(value = "/category/{categoryName}")
    public ResponseEntity<CategoryResponse> categoryName(@PathVariable String categoryName) {

        return categoryService.findByCategory(categoryName)
                .map(CategoryResponse::fromCategory)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
