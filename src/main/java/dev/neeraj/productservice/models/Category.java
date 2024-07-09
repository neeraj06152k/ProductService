package dev.neeraj.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Category extends BaseModel{
    private String name;

    //@OneToMany(mappedBy = "category")
    //private List<Product> products;
}
