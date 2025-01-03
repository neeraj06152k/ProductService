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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity<SentProductDTO> getProduct(@PathVariable long id)
            throws ProductNotFoundException {

        Product product = productService.getProduct(id);

        return new ResponseEntity<>(
                SentProductDTO.toSentProductDTO(product),
                HttpStatus.OK
        );
    }

    @GetMapping("/")
    public ResponseEntity<ListSentProductDTO> getAllProducts() throws ProductNotFoundException {

        List<Product> productList = productService.getAllProducts();

        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(
                productList.stream()
                        .map(SentProductDTO::toSentProductDTO)
                        .collect(Collectors.toList())
        );

        return new ResponseEntity<>(listSentProductDTO, HttpStatus.OK);
    }

    @GetMapping("/limited")
    public ResponseEntity<ListSentProductDTO> getLimitedProducts(
            @RequestParam(defaultValue = "10") int limit
    ) throws ProductNotFoundException {
        List<Product> products = productService.getLimitedProducts(limit);

        ListSentProductDTO listSentProductDTO = new ListSentProductDTO();
        listSentProductDTO.setDtoList(products.stream()
                .map(SentProductDTO::toSentProductDTO)
                .toList());

        return new ResponseEntity<>(
                listSentProductDTO,
                HttpStatus.OK
        );
    }


}
