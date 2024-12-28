package me.songjiyoung.dto.admin.response;

import lombok.Builder;
import lombok.Getter;
import me.songjiyoung.entity.Product;

@Getter
@Builder
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private Long brandId;
    private Integer price;
    private String name;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .price(product.getPrice())
                .name(product.getName())
                .build();

    }
}
