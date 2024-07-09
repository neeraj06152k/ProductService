package dev.neeraj.productservice.dtos;

import dev.neeraj.productservice.models.Category;
import dev.neeraj.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceivedProductDTO {
    private long id;
    private double price;
    private String title;
    private String description;
    private String image;
    private String category;

    public Product convertToProduct(){
        Product product = new Product();
        product.setId(id);
        product.setPrice(price);
        product.setTitle(title);
        product.setDescription(description);
        product.setImageURL(image);
        Category category = new Category();
        category.setName(this.category);
        product.setCategory(category);

        return product;
    }
}
