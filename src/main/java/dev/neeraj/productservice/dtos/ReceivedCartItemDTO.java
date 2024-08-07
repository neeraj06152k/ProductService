package dev.neeraj.productservice.dtos;

import dev.neeraj.productservice.models.CartItem;
import dev.neeraj.productservice.repositories.ProductRepository;
import dev.neeraj.productservice.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

@Getter
@Setter
public class ReceivedCartItemDTO {
    private long productId;
    private long userId;
    private int quantity;
}
