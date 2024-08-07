package dev.neeraj.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "cart",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "product_id"})
        })
public class CartItem extends BaseModel{
    @ManyToOne(targetEntity = User.class, optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)

    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(targetEntity = Product.class, optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}
