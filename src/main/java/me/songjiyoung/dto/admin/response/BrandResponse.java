package me.songjiyoung.dto.admin.response;

import me.songjiyoung.entity.Brand;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BrandResponse {

    private Long id;
    private String name;

    public static BrandResponse of(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();

    }
}
