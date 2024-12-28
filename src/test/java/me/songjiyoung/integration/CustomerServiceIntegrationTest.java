package me.songjiyoung.integration;

import me.songjiyoung.ShoppingApplication;
import me.songjiyoung.dto.admin.request.BrandRequest;
import me.songjiyoung.dto.admin.response.BrandResponse;
import me.songjiyoung.dto.customer.response.LowestHighestPriceByCategory;
import me.songjiyoung.dto.customer.response.LowestPriceByBrand;
import me.songjiyoung.dto.customer.response.LowestPriceByCategory;
import me.songjiyoung.exception.ApplicationException;
import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import me.songjiyoung.repository.BrandRepository;
import me.songjiyoung.repository.CategoryRepository;
import me.songjiyoung.repository.ProductRepository;
import me.songjiyoung.service.admin.AdminService;
import me.songjiyoung.service.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShoppingApplication.class)
@Transactional
@ActiveProfiles("test")
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void initializeTestData() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();

        Brand brandOne = createBrand("BrandOne");
        Brand brandTwo = createBrand("BrandTwo");

        Category categoryTop = createCategory("Top");
        Category categoryBottom = createCategory("Bottom");

        createProduct(brandOne, categoryTop, 11000, "Top Product One");
        createProduct(brandOne, categoryBottom, 21000, "Bottom Product One");
        createProduct(brandTwo, categoryTop, 16000, "Top Product Two");
        createProduct(brandTwo, categoryBottom, 26000, "Bottom Product Two");
    }

    private Brand createBrand(String name) {
        return brandRepository.save(new Brand(null, name));
    }

    private Category createCategory(String name) {
        return categoryRepository.save(new Category(null, name));
    }

    private Product createProduct(Brand brand, Category category, int price, String name) {
        return productRepository.save(new Product(null, brand, category, price, name));
    }

    /**
     * 카테고리별 최저가 상품 조회
     */
    @Test
    void shouldRetrieveLowestPriceByCategory() {
        LowestPriceByCategory result = customerService.getLowestPriceByCategory();

        assertNotNull(result);
        assertEquals(2, result.getCategoryPrices().size());
        assertEquals(32000, result.getTotalPrice());

        List<LowestPriceByCategory.CategoryPrice> categoryPrices = result.getCategoryPrices();
        LowestPriceByCategory.CategoryPrice firstCategory = categoryPrices.get(0);
        assertEquals("Top", firstCategory.getCategory());
        assertEquals("BrandOne", firstCategory.getBrand());
        assertEquals(11000, firstCategory.getPrice());

        LowestPriceByCategory.CategoryPrice secondCategory = categoryPrices.get(1);
        assertEquals("Bottom", secondCategory.getCategory());
        assertEquals("BrandOne", secondCategory.getBrand());
        assertEquals(21000, secondCategory.getPrice());
    }

    /**
     * 전체 브랜드 중 최저가 브랜드 조회
     */
    @Test
    void shouldRetrieveLowestPriceBrand() {
        LowestPriceByBrand result = customerService.getLowestPriceByBrand();

        assertNotNull(result);
        assertEquals("BrandOne", result.getLowestPrice().getBrand());
        assertEquals(32000, result.getLowestPrice().getTotalPrice());
    }

    /**
     * 특정 카테고리에서 최저가 및 최고가 상품 조회
     */
    @Test
    void shouldRetrievePriceRangeForCategory() {
        LowestHighestPriceByCategory result = customerService.getLowestHighestPriceByCategory("Top");

        assertNotNull(result);
        assertEquals("Top", result.getCategory());
        assertEquals("BrandOne", result.getLowestPrice().getBrand());
        assertEquals(11000, result.getLowestPrice().getPrice());
        assertEquals("BrandTwo", result.getHighestPrice().getBrand());
        assertEquals(16000, result.getHighestPrice().getPrice());
    }

    /**
     * 새 브랜드 추가
     */
    @Test
    void shouldAddNewBrand() {
        BrandRequest newBrandRequest = BrandRequest.builder().name("BrandThree").build();
        BrandResponse newBrand = adminService.createBrand(newBrandRequest);

        assertNotNull(newBrand);
        assertEquals("BrandThree", newBrand.getName());
        assertTrue(brandRepository.findById(newBrand.getId()).isPresent());
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateBrand() {
        BrandRequest duplicateBrandRequest = BrandRequest.builder().name("BrandOne").build();

        assertThrows(ApplicationException.class, () -> adminService.createBrand(duplicateBrandRequest));
    }

    /**
     * 브랜드 업데이트
     */
    @Test
    void shouldUpdateBrand() {
        Brand existingBrand = brandRepository.findByName("BrandOne").orElseThrow();

        BrandRequest updateRequest = BrandRequest.builder().name("UpdatedBrandOne").build();
        BrandResponse updatedBrand = adminService.updateBrand(updateRequest, existingBrand.getId());

        assertNotNull(updatedBrand);
        assertEquals("UpdatedBrandOne", updatedBrand.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentBrand() {
        BrandRequest updateRequest = BrandRequest.builder().name("NonExistentBrand").build();

        assertThrows(ApplicationException.class, () -> adminService.updateBrand(updateRequest, 999L));
    }

    /**
     * 브랜드 삭제
     */
    @Test
    void shouldDeleteBrand() {
        Brand brandToDelete = brandRepository.findByName("BrandTwo").orElseThrow();
        List<Product> products = productRepository.findByBrand(brandToDelete);
        productRepository.deleteAll(products);

        adminService.deleteBrand(brandToDelete.getId());

        assertFalse(brandRepository.findById(brandToDelete.getId()).isPresent());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBrand() {
        assertThrows(ApplicationException.class, () -> adminService.deleteBrand(999L));
    }
}
