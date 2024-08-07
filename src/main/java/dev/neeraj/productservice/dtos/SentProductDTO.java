package dev.neeraj.productservice.dtos;

import dev.neeraj.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SentProductDTO {
    private long id;
    private double price;
    private int quantity;
    private String title;
    private String description;
    private String image;
    private String category;

    public static SentProductDTO toSentProductDTO(Product product){
        SentProductDTO sentProductDTO = new SentProductDTO();
        sentProductDTO.setId(product.getId());
        sentProductDTO.setTitle(product.getTitle());
        sentProductDTO.setPrice(product.getPrice());
        sentProductDTO.setQuantity(product.getQuantity());
        sentProductDTO.setCategory(product.getCategory().getName());
        sentProductDTO.setDescription(product.getDescription());
        sentProductDTO.setImage(product.getImageURL());

        return sentProductDTO;
    }
}
