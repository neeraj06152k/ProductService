package dev.neeraj.productservice.controllers;

import dev.neeraj.productservice.dtos.ListCategoryDTO;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ProductService productService;

    public CategoryController(
            @Qualifier("realProductService") ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<ListCategoryDTO> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        return new ResponseEntity<>(new ListCategoryDTO(categories), HttpStatus.OK);
    }
}
