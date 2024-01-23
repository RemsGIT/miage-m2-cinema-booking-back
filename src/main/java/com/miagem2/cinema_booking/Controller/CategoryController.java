package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.Model.Category;
import com.miagem2.cinema_booking.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId, @RequestBody Category categoryDetails) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            // Mettez à jour les détails de la catégorie
            category.setName(categoryDetails.getName());

            return categoryRepository.save(category);
        }

        return null;
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
