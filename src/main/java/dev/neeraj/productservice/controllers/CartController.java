package dev.neeraj.productservice.controllers;

import dev.neeraj.productservice.dtos.ReceivedCartItemDTO;
import dev.neeraj.productservice.dtos.ReceivedUserDTO;
import dev.neeraj.productservice.models.CartItem;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.models.User;
import dev.neeraj.productservice.repositories.CartRepository;
import dev.neeraj.productservice.repositories.ProductRepository;
import dev.neeraj.productservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @PatchMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody ReceivedCartItemDTO cartItemDTO) {

        CartItem existingCartItem = cartRepository.findByUserIdAndProductId(
                cartItemDTO.getUserId(), cartItemDTO.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(cartItemDTO.getQuantity());
            return ResponseEntity.ok(cartRepository.save(existingCartItem));
        } else {

            User user = userRepository.findById(cartItemDTO.getUserId());
            Product product = productRepository.findById(cartItemDTO.getProductId());

            if (user == null || product == null) {
                System.out.println("user:"+(user!=null)+" product:"+(product!=null));
                return ResponseEntity.badRequest().build();
            }

            CartItem newCartItem = new CartItem();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartItemDTO.getQuantity());

            return ResponseEntity.ok(cartRepository.save(newCartItem));
        }
    }

    @PostMapping("/getCartItems")
    public ResponseEntity<CartItem[]> getCartItems(@RequestBody ReceivedUserDTO userDTO) {
        List<CartItem> cartItems = cartRepository.findAllByUserId(userDTO.getUserId());

        return ResponseEntity.ok(cartItems.toArray(new CartItem[0]));
    }
}
