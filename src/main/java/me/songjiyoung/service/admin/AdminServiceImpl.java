package me.songjiyoung.service.admin;

import lombok.RequiredArgsConstructor;
import me.songjiyoung.dto.admin.request.BrandRequest;
import me.songjiyoung.dto.admin.request.ProductRequest;
import me.songjiyoung.dto.admin.response.BrandResponse;
import me.songjiyoung.dto.admin.response.ProductResponse;
import me.songjiyoung.exception.ApplicationException;
import me.songjiyoung.exception.ErrorMessage;
import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import me.songjiyoung.repository.BrandRepository;
import me.songjiyoung.repository.CategoryRepository;
import me.songjiyoung.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest brandRequest) {
        try {
            if (brandRepository.findByName(brandRequest.getName()).isPresent()) {
                throw new ApplicationException(ErrorMessage.BRAND_ALREADY_EXISTS, brandRequest.getName());
            }

            Brand brand = Brand.builder()
                    .name(brandRequest.getName())
                    .build();
            return BrandResponse.of(brandRepository.save(brand));
        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.DATA_UPSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public BrandResponse updateBrand(BrandRequest brandRequest, Long brandId) {
        try {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.BRAND_NOT_FOUND));

            if (brandRepository.findByName(brandRequest.getName())
                    .filter(existingBrand -> !existingBrand.getId().equals(brandId))
                    .isPresent()) {
                throw new ApplicationException(ErrorMessage.BRAND_ALREADY_EXISTS, brandRequest.getName());
            }

            brand.setName(brandRequest.getName());
            return BrandResponse.of(brandRepository.save(brand));

        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.DATA_UPSERT_ERROR, e);
        }
    }

    @Override
    @Transactional
    public BrandResponse deleteBrand(Long brandId) {
        try {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.BRAND_NOT_FOUND));

            boolean hasProducts = productRepository.existsByBrandId(brandId);

            if (hasProducts) {
                throw new ApplicationException(ErrorMessage.BRAND_HAS_PRODUCTS, brand.getName());
            }

            brandRepository.deleteById(brandId);

            return BrandResponse.of(brand);

        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.DATA_UPSERT_ERROR, e);
        }
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.BRAND_NOT_FOUND, productRequest.getBrandId()));

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.CATEGORY_NOT_FOUND, productRequest.getCategoryId()));

            Product product = Product.builder()
                    .category(category)
                    .brand(brand)
                    .price(productRequest.getPrice())
                    .name(productRequest.getName())
                    .build();

            return ProductResponse.of(productRepository.save(product));

        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.DATA_UPSERT_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest, Long productId) {
        try {
            Brand brand = brandRepository.findById(productRequest.getBrandId())
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.BRAND_NOT_FOUND, productRequest.getBrandId()));

            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.CATEGORY_NOT_FOUND, productRequest.getCategoryId()));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.PRODUCT_NOT_FOUND, productId));

            product.setBrand(brand);
            product.setCategory(category);
            product.setPrice(productRequest.getPrice());
            product.setName(productRequest.getName());

            return ProductResponse.of(productRepository.save(product));

        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.DATA_UPSERT_ERROR, e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ApplicationException(ErrorMessage.PRODUCT_NOT_FOUND, productId));

            productRepository.deleteById(productId);

            return ProductResponse.of(product);

        } catch (DataIntegrityViolationException e) {
            throw new ApplicationException(ErrorMessage.PRODUCT_HAS_DEPENDENCIES, e.getMessage());
        }
    }

    @Override
    public List<String> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(Brand::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllProducts() {
        return productRepository.findAll().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }
}