package dev.neeraj.productservice.repositories;

import dev.neeraj.productservice.models.CartItem;
import dev.neeraj.productservice.models.Product;
import dev.neeraj.productservice.models.User;
import jakarta.persistence.Column;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    CartItem save(@NonNull CartItem cartItem);
    CartItem findById(long id);
    List<CartItem> findAllByUserId(long userId);
    void deleteAllByUserId(long userId);
    CartItem findByUserIdAndProductId(long userId, long productId);
}
