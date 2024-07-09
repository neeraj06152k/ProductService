package dev.neeraj.productservice.repositories;

import dev.neeraj.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category save(Category category);
    Category findByName(String name);
    void deleteAll();
    List<Category> getAllByIsDeleted(boolean isDeleted);
}
