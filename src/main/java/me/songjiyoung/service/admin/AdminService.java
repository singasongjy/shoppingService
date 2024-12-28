package me.songjiyoung.service.admin;

import me.songjiyoung.dto.admin.request.BrandRequest;
import me.songjiyoung.dto.admin.request.ProductRequest;
import me.songjiyoung.dto.admin.response.BrandResponse;
import me.songjiyoung.dto.admin.response.ProductResponse;

import java.util.List;

public interface AdminService {
    BrandResponse createBrand(BrandRequest brandRequest);
    BrandResponse updateBrand(BrandRequest brandRequest, Long brandId);
    BrandResponse deleteBrand(Long brandId);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(ProductRequest productRequest, Long productId);
    ProductResponse deleteProduct(Long productId);
    List<String> getAllCategories();
    List<String> getAllBrands();
    List<String> getAllProducts();
}