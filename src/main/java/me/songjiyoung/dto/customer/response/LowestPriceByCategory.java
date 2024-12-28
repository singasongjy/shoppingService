package me.songjiyoung.dto.customer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LowestPriceByCategory {
    @JsonProperty("카테고리별 최저가격")
    private List<CategoryPrice> categoryPrices;

    @JsonProperty("총액")
    private int totalPrice;

    @Data
    public static class CategoryPrice {
        @JsonProperty("카테고리")
        private String category;

        @JsonProperty("브랜드")
        private String brand;

        @JsonProperty("가격")
        private int price;

        public CategoryPrice(String category, String brand, Integer price) {
            this.category = category;
            this.brand = brand;
            this.price = price;
        }
    }
}