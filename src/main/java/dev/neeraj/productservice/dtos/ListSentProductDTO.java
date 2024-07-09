package dev.neeraj.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSentProductDTO {
    private List<SentProductDTO> dtoList;
}
