package me.songjiyoung.controller.admin;

import me.songjiyoung.dto.admin.request.BrandRequest;
import me.songjiyoung.dto.admin.request.ProductRequest;
import me.songjiyoung.dto.admin.response.BrandResponse;
import me.songjiyoung.dto.admin.response.ProductResponse;
import me.songjiyoung.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "브랜드 등록", description = "신규 브랜드를 등록합니다.")
    @PostMapping("/brand")
    public ResponseEntity<BrandResponse> addBrand(@RequestBody BrandRequest brandRequest) {
        BrandResponse newBrand = adminService.createBrand(brandRequest);
        return ResponseEntity.ok(newBrand);
    }

    @Operation(summary = "브랜드 수정", description = "브랜드 정보를 수정합니다.")
    @PutMapping("/brand/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(@RequestBody BrandRequest brandRequest, @PathVariable Long brandId) {
        BrandResponse updatedBrand = adminService.updateBrand(brandRequest, brandId);
        return ResponseEntity.ok(updatedBrand);
    }

    @Operation(summary = "브랜드 삭제", description = "특정 브랜드를 삭제합니다.")
    @DeleteMapping("/brand/{brandId}")
    public ResponseEntity<BrandResponse> deleteBrand(@PathVariable Long brandId) {
        BrandResponse updateBrand = adminService.deleteBrand(brandId);
        return ResponseEntity.ok(updateBrand);
    }

    @Operation(summary = "상품 등록", description = "신규 상품을 등록합니다.")
    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse newProduct = adminService.createProduct(productRequest);
        return ResponseEntity.ok(newProduct);
    }

    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long productId) {
        ProductResponse updatedBrand = adminService.updateProduct(productRequest, productId);
        return ResponseEntity.ok(updatedBrand);
    }

    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제합니다.")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId) {
        ProductResponse updateProduct = adminService.deleteProduct(productId);
        return ResponseEntity.ok(updateProduct);
    }

    @Operation(summary = "카테고리 조회", description = "모든 카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = adminService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "브랜드 조회", description = "모든 브랜드 목록을 조회합니다.")
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        List<String> brands = adminService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @Operation(summary = "상품 조회", description = "모든 상품 목록을 조회합니다.")
    @GetMapping("/products")
    public ResponseEntity<List<String>> getProducts() {
        List<String> products = adminService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}