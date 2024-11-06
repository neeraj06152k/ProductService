package dev.neeraj.productservice.services;

import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.repositories.CategoryRepository;
import dev.neeraj.productservice.repositories.ProductRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Primary
@Service
@Getter
public class RealProductService implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CacheService cacheService;

    @Autowired
    public RealProductService(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              CacheService cacheService){
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;
        this.cacheService = cacheService;
    }

    @Override
    public Product getProduct(long id) throws ProductNotFoundException {
        Product cachedProduct = (Product) cacheService.get("product", "PRODUCT_" + id).orElse(null);

        if(cachedProduct!=null){
            return cachedProduct;
        }

        Product product = getProductFromDB(id);
        cacheService.put("product", "PRODUCT_" + id, product);
        return product;
    }
    public Product getProductFromDB(long id) throws ProductNotFoundException {
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
        List<Object> productList = cacheService.values("product");
        if(!productList.isEmpty()){
            return productList.stream()
                    .map(product -> (Product) product)
                    .toList();
        }

        List<Product> products = getAllProductsFromDB();
        for(Product product: products){
            cacheService.put("product", "PRODUCT_" + product.getId(), product);
        }
        return products;
    }
    public List<Product> getAllProductsFromDB() throws ProductNotFoundException {
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
        cacheService.clear("product");
    }
}
