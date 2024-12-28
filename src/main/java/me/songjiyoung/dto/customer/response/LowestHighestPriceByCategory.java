package me.songjiyoung.dto.customer.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LowestHighestPriceByCategory {
    @JsonProperty("카테고리")
    private String category;

    @JsonProperty("최저가")
    private BrandPrice lowestPrice;

    @JsonProperty("최고가")
    private BrandPrice highestPrice;

    @Data
    public static class BrandPrice {
        @JsonProperty("브랜드")
        private String brand;

        @JsonProperty("가격")
        private int price;
    }
}