package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.model.Category;
import com.gabriel.workshop_api.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Category category) {

        Category foundCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new EntityNotFoundException("Esta categoria não existe"));

        categoryRepository.findById(category.getId()).ifPresent(categoryExist ->{
            if(!foundCategory.getId().equals(category.getId())) {
                throw new IllegalArgumentException("Este categoria já pertence a outra categoria");
            }
        });

        foundCategory.updateCategory(category);

        return categoryRepository.save(foundCategory);
    }

    @Transactional
    public void delete(Long id) {

        if(!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Esta categoria não existe para ser excluído");
        }

        categoryRepository.deleteById(id);
    }
}
