package dev.neeraj.productservice.services;

import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;

import java.util.List;

public interface ProductService {
    public Product getProduct(long id) throws ProductNotFoundException;
    public List<Product> getAllProducts() throws ProductNotFoundException;
    public Product addProduct(Product product) throws ProductCreationFailedException, ProductNotFoundException;
    public List<Category> getAllCategories();
    public List<Product> getLimitedProducts(int limit) throws ProductNotFoundException;
}
