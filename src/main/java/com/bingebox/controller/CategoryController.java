package com.bingebox.controller;

import com.bingebox.controller.request.CategoryRequest;
import com.bingebox.controller.response.CategoryResponse;
import com.bingebox.entity.Category;
import com.bingebox.mapper.CategoryMapper;
import com.bingebox.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bingebox/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> list = categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody CategoryRequest request) {
        Category newCategory = CategoryMapper.toCategory(request);
        Category savedCategory = categoryService.saveCategory(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryMapper.toCategoryResponse(savedCategory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getByCategoryId(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(category -> ResponseEntity.ok(CategoryMapper.toCategoryResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByCategoryId(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
