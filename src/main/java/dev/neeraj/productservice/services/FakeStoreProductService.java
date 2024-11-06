package dev.neeraj.productservice.services;

import dev.neeraj.productservice.dtos.ReceivedProductDTO;
import dev.neeraj.productservice.dtos.SentProductDTO;
import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FakeStoreProductService implements ProductService {

    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    public FakeStoreProductService(
            RestTemplate restTemplate,
            CacheService cacheService
    ) {
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
    }


    @Override
    public Product getProduct(long id) throws ProductNotFoundException {

        Product cachedProduct = cacheService.get("product", "PRODUCT_" + id)
                .map(Product.class::cast).orElse(null);

        if (cachedProduct != null) {
            return cachedProduct;
        }

        ReceivedProductDTO receivedProductDTO = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + id,
                ReceivedProductDTO.class
        );

        if (receivedProductDTO == null)
            throw new ProductNotFoundException(
                    "Product with id "
                            + id
                            + " not found"
            );

        Product product = receivedProductDTO.toProduct();
        cacheService.put("product", "PRODUCT_" + id, product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() throws ProductNotFoundException {

        List<Product> allProducts = cacheService.values("product").stream()
                .map(product -> (Product) product)
                .toList();
        if(!allProducts.isEmpty()){
            return allProducts;
        }


        ReceivedProductDTO[] arrayReceivedProductDTO = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                ReceivedProductDTO[].class
        );

        if (arrayReceivedProductDTO == null || arrayReceivedProductDTO.length == 0) {
            throw new ProductNotFoundException(
                    "No Products found"
            );
        }

        List<Product> products = Arrays.stream(arrayReceivedProductDTO)
                .map(ReceivedProductDTO::toProduct)
                .collect(Collectors.toList());

        //cacheService.put("product", "ALL_PRODUCTS", products);
        products.forEach(product -> cacheService.put("product", "PRODUCT_" + product.getId(), product));

        return products;
    }

    @Override
    public Product addProduct(Product product) throws ProductCreationFailedException, ProductNotFoundException {
        if (product == null)
            throw new ProductNotFoundException(
                    "Input Product Object cannot be null"
            );

        ReceivedProductDTO receivedProductDTO = restTemplate.postForObject(
                "https://fakestoreapi.com/products/",
                SentProductDTO.toSentProductDTO(product),
                ReceivedProductDTO.class
        );

        if (receivedProductDTO == null)
            throw new ProductCreationFailedException(
                    "Unable to create new Product"
            );

        return receivedProductDTO.toProduct();
    }

    @Override
    public List<Category> getAllCategories() {
        String[] categories = restTemplate.getForObject(
                "https://fakestoreapi.com/products/categories",
                String[].class
        );
        assert categories != null : "Category array cannot be null";
        return Arrays.stream(categories)
                .map(str -> {
                    var newCategory = new Category();
                    newCategory.setName(str);
                    return newCategory;
                })
                .toList();
    }

    @Override
    public List<Product> getLimitedProducts(int limit) throws ProductNotFoundException {

        ReceivedProductDTO[] receivedProductDTOs = restTemplate.getForObject(
                "https://fakestoreapi.com/products?limit=" + limit,
                ReceivedProductDTO[].class
        );
        if (receivedProductDTOs == null) {
            throw new ProductNotFoundException("No products in FakeStore");
        }

        return Arrays.stream(receivedProductDTOs).map(ReceivedProductDTO::toProduct).toList();
    }
}
