package dev.neeraj.productservice.services;

import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.repositories.CategoryRepository;
import dev.neeraj.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealProductService implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public RealProductService(ProductRepository productRepository,
                              CategoryRepository categoryRepository){
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public Product getProduct(long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id);
        if(product==null){
            throw new ProductNotFoundException(
                    "Product with ID "
                    + id
                    + " not found in product repository"
            );
        }

        return product;
    }

    @Override
    public List<Product> getAllProducts() throws ProductNotFoundException {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product) throws ProductCreationFailedException, ProductNotFoundException {
        Category categoryFromDB = categoryRepository.findByName(product.getCategory().getName());
        if(categoryFromDB==null){
            Category newcategory = new Category();
            newcategory.setName(product.getCategory().getName());
            categoryFromDB = categoryRepository.save(newcategory);
        }

        product.setCategory(categoryFromDB);

        return productRepository.save(product);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllByIsDeleted(false);
    }

    @Override
    public List<Product> getLimitedProducts(int limit) throws ProductNotFoundException {
        return productRepository.findLimitedProducts(limit);
    }


    public void deleteAllProducts(){
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
