package com.project.shopapp.services.category;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Optional<Category> getCategoryById(long id) {
        return this.categoryRepository.findById(id);
    }
    @Override
    public List<Category> getAllCategories() {

        return this.categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow();

        existingCategory.setName(categoryDTO.getName());

        return categoryRepository.save(existingCategory);
    }

//        if (category.isPresent()) {
//            category.get().setName(categoryDTO.getName());
//        }
//        return categoryRepository.save(category.get());
//    }
    @Override
    public Optional<Category> deleteCategory(long id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        }
        return category;
    }
}
