package me.songjiyoung.dto.admin.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {

    @NotBlank(message = "브랜드명을 입력해주세요.")
    @Size(min = 1, max = 100)
    private String name;
}
