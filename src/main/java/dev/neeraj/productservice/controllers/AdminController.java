package dev.neeraj.productservice.controllers;

import dev.neeraj.productservice.dtos.ListSentProductDTO;
import dev.neeraj.productservice.dtos.ReceivedProductDTO;
import dev.neeraj.productservice.dtos.SentProductDTO;
import dev.neeraj.productservice.exceptions.ProductCreationFailedException;
import dev.neeraj.productservice.exceptions.ProductNotFoundException;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.services.FakeStoreProductService;
import dev.neeraj.productservice.services.ProductService;
import dev.neeraj.productservice.services.RealProductService;
import dev.neeraj.productservice.services.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final RestTemplate restTemplate;
    private final CacheService cacheService;


    @PostMapping("/add")
    public ResponseEntity<SentProductDTO> addProduct(@RequestBody ReceivedProductDTO receivedProductDTO)
            throws ProductCreationFailedException, ProductNotFoundException {
        Product product = receivedProductDTO.toProduct();
        Product createdProduct = productService.addProduct(product);

        return new ResponseEntity<>(SentProductDTO.toSentProductDTO(createdProduct), HttpStatus.OK);
    }

    @GetMapping("/resetRealDB")
    public ResponseEntity<ListSentProductDTO> resetRealDB()
            throws ProductNotFoundException, ProductCreationFailedException {
        if(productService instanceof FakeStoreProductService) return ResponseEntity.ok(null);


        FakeStoreProductService fakeStoreProductService = new FakeStoreProductService(
                restTemplate, cacheService
        );


        List<Product> allFakeStoreProducts = fakeStoreProductService.getAllProducts();

        RealProductService realProductService = (RealProductService) productService;
        realProductService.deleteAllProducts();

        for(Product fakeProduct: allFakeStoreProducts){
            fakeProduct.setQuantity(10);
            productService.addProduct(fakeProduct);
        }

        List<Product> realDBProducts = productService.getAllProducts();
        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(
                realDBProducts.stream()
                        .map(SentProductDTO::toSentProductDTO)
                        .toList()
        );

        return new ResponseEntity<>(listSentProductDTO, HttpStatus.OK);
    }
}
