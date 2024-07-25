package dev.neeraj.productservice.models;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseModel implements Serializable {
    private double price;
    private String title;
    @Column(length = 1024)
    private String description;
    private String imageURL;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}
