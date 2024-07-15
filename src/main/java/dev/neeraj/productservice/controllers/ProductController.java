package dev.neeraj.productservice.controllers;

import dev.neeraj.productservice.ProductServiceApplication;
import dev.neeraj.productservice.dtos.ListCategoryDTO;
import dev.neeraj.productservice.dtos.ListSentProductDTO;
import dev.neeraj.productservice.dtos.ReceivedProductDTO;
import dev.neeraj.productservice.dtos.SentProductDTO;
import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.repositories.ProductRepository;
import dev.neeraj.productservice.services.FakeStoreProductService;
import dev.neeraj.productservice.services.ProductService;
import dev.neeraj.productservice.services.RealProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Qualifier("realProductService")
    @Autowired
    ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<SentProductDTO> getProduct(@PathVariable long id) throws ProductNotFoundException {

        Product product = productService.getProduct(id);

        return new ResponseEntity<>(
                SentProductDTO.convertProductToSentProductDTO(product),
                HttpStatus.OK
        );
    }

    @GetMapping("/products")
    public ResponseEntity<ListSentProductDTO> getAllProducts() throws ProductNotFoundException {

        List<Product> productList = productService.getAllProducts();

        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(
                productList.stream()
                        .map(SentProductDTO::convertProductToSentProductDTO)
                        .collect(Collectors.toList())
        );

        return new ResponseEntity<>(listSentProductDTO, HttpStatus.OK);
    }

    @GetMapping("/products/limited")
    public ResponseEntity<ListSentProductDTO> getLimitedProducts(@RequestParam(defaultValue = "10") int limit) throws ProductNotFoundException {
        List<Product> products = productService.getLimitedProducts(limit);

        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(products.stream()
                .map(SentProductDTO::convertProductToSentProductDTO)
                .toList());

        return new ResponseEntity<>(
                listSentProductDTO,
                HttpStatus.OK
        );
    }

    @PostMapping("/products")
    public ResponseEntity<SentProductDTO> addProduct(@RequestBody ReceivedProductDTO receivedProductDTO) throws ProductCreationFailedException, ProductNotFoundException {
        Product product = receivedProductDTO.convertToProduct();
        Product createdProduct = productService.addProduct(product);

        return new ResponseEntity<>(SentProductDTO.convertProductToSentProductDTO(createdProduct), HttpStatus.OK);
    }

    @GetMapping("/resetRealDB")
    public ResponseEntity<ListSentProductDTO> resetRealDB() throws ProductNotFoundException, ProductCreationFailedException {
        if(productService instanceof FakeStoreProductService) return null;

        FakeStoreProductService fakeStoreProductService = new FakeStoreProductService(new RestTemplate());

        List<Product> allFakeStoreProducts = fakeStoreProductService.getAllProducts();

        RealProductService realProductService = (RealProductService) productService;
        realProductService.deleteAllProducts();

        for(Product fakeProduct: allFakeStoreProducts){
            productService.addProduct(fakeProduct);
        }

        List<Product> realDBProducts = productService.getAllProducts();
        System.out.println("\n"+productService.getAllProducts().size());
        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(
                realDBProducts.stream().map(SentProductDTO::convertProductToSentProductDTO).toList()
        );

        return new ResponseEntity<>(listSentProductDTO, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<ListCategoryDTO> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        return new ResponseEntity<>(new ListCategoryDTO(categories), HttpStatus.OK);
    }


}
