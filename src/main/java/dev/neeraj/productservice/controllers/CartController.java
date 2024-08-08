package dev.neeraj.productservice.controllers;

import dev.neeraj.productservice.dtos.ReceivedCartItemDTO;
import dev.neeraj.productservice.dtos.ReceivedProductDTO;
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

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/getInCartProducts")
    public ResponseEntity<ReceivedProductDTO[]> getInCartProducts(@RequestBody ReceivedUserDTO userDTO) {
        List<CartItem> cartItems = cartRepository.findAllByUserId(userDTO.getUserId());
        List<ReceivedProductDTO> cartProducts = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ReceivedProductDTO productDTO = new ReceivedProductDTO();
            productDTO.setId(cartItem.getProduct().getId());
            productDTO.setTitle(cartItem.getProduct().getTitle());
            productDTO.setPrice(cartItem.getProduct().getPrice());
            productDTO.setDescription(cartItem.getProduct().getDescription());
            productDTO.setImage(cartItem.getProduct().getImageURL());
            productDTO.setCategory(cartItem.getProduct().getCategory().getName());
            productDTO.setQuantity(cartItem.getQuantity());
            cartProducts.add(productDTO);
        }
        return ResponseEntity.ok(cartProducts.toArray(new ReceivedProductDTO[0]));
    }
}
