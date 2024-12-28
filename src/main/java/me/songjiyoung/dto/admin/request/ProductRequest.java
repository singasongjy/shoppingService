package me.songjiyoung.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = "브랜드ID를 입력 해주세요.")
    private Long brandId;
    @NotNull(message = "카테고리ID를 입력 해주세요.")
    private Long categoryId;
    @NotNull(message = "상품의 가격을 입력 해주세요.")
    private Integer price;
    @NotBlank(message = "상품명을 입력 해주세요.")
    @Size(min = 1, max = 100)
    private String name;
}
