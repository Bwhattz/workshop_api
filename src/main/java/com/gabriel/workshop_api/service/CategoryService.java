package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.model.Category;
import com.gabriel.workshop_api.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Category> findByCategory(String categoryName) {
        return categoryRepository.findByCategory(categoryName);
    }

    @Transactional
    public Category update(Category category) {

        Category foundCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new EntityNotFoundException("Esta categoria não existe."));

        categoryRepository.findById(category.getId()).ifPresent(categoryExist ->{
            if(!categoryExist.getId().equals(foundCategory.getId())) {
                throw new IllegalArgumentException("Este categoria já pertence a outra categoria.");
            }
        });

        foundCategory.updateCategory(category);

        return categoryRepository.save(foundCategory);
    }

    @Transactional
    public void delete(Long id) {

        if(!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Esta categoria não existe para ser excluído.");
        }

        categoryRepository.deleteById(id);
    }
}
