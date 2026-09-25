package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
            value = "SELECT category.* FROM category AS category" +
                    "WHERE category.name = :name",
            nativeQuery = true
    )
    Optional<Category> findByCategory(@Param("category_name") String categoryName);
}
