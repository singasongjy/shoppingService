package me.songjiyoung.dto.customer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LowestPriceByBrand {
    @JsonProperty("최저가")
    private LowestPrice lowestPrice;

    @Data
    public static class LowestPrice {
        @JsonProperty("브랜드")
        private String brand;

        @JsonProperty("카테고리")
        private List<CategoryPrice> categories;

        @JsonProperty("총액")
        private int totalPrice;
    }

    @Data
    public static class CategoryPrice {
        @JsonProperty("카테고리")
        private String category;

        @JsonProperty("가격")
        private int price;

        public CategoryPrice(String category, Integer price) {
            this.category = category;
            this.price = price;
        }
    }
}