package me.songjiyoung.controller.customer;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.songjiyoung.dto.customer.response.LowestHighestPriceByCategory;
import me.songjiyoung.dto.customer.response.LowestPriceByBrand;
import me.songjiyoung.dto.customer.response.LowestPriceByCategory;
import me.songjiyoung.service.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "카테고리별 최저가 정보 조회", description = "각 카테고리별 최저가 브랜드와 가격, 총액을 조회합니다.")
    @GetMapping("/category/lowest-price")
    public ResponseEntity<LowestPriceByCategory> getLowestPriceByCategory() {
        LowestPriceByCategory result = customerService.getLowestPriceByCategory();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "단일 브랜드 최저가 정보 조회", description = "모든 카테고리 상품을 구매할 때 최저가 단일 브랜드와 가격, 총액을 조회합니다.")
    @GetMapping("/brand/lowest-price")
    public ResponseEntity<LowestPriceByBrand> getLowestPriceByBrand() {
        LowestPriceByBrand result = customerService.getLowestPriceByBrand();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "카테고리별 최저/최고가 브랜드 조회", description = "특정 카테고리의 최저/최고가 브랜드와 가격을 조회합니다.")
    @GetMapping("/category/lowest-highest")
    public ResponseEntity<LowestHighestPriceByCategory> getLowestHighestPriceByCategory(@RequestParam String category) {
        LowestHighestPriceByCategory result = customerService.getLowestHighestPriceByCategory(category);
        return ResponseEntity.ok(result);
    }
}