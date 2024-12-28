package service.admin;

import me.songjiyoung.dto.admin.request.BrandRequest;
import me.songjiyoung.dto.admin.request.ProductRequest;
import me.songjiyoung.dto.admin.response.BrandResponse;
import me.songjiyoung.dto.admin.response.ProductResponse;
import me.songjiyoung.exception.ApplicationException;
import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import me.songjiyoung.repository.BrandRepository;
import me.songjiyoung.repository.CategoryRepository;
import me.songjiyoung.repository.ProductRepository;
import me.songjiyoung.service.admin.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Brand brandAlpha, brandBeta;
    private Category categoryShoes, categoryBags;
    private Product productAlphaShoes, productAlphaBags, productBetaShoes, productBetaBags;

    @BeforeEach
    void initializeTestData() {
        brandAlpha = new Brand(101L, "BrandAlpha");
        brandBeta = new Brand(102L, "BrandBeta");
        categoryShoes = new Category(201L, "신발");
        categoryBags = new Category(202L, "가방");
        productAlphaShoes = new Product(301L, brandAlpha, categoryShoes, 12000, "A신발");
        productAlphaBags = new Product(302L, brandAlpha, categoryBags, 22000, "A가방");
        productBetaShoes = new Product(303L, brandBeta, categoryShoes, 18000, "B신발");
        productBetaBags = new Product(304L, brandBeta, categoryBags, 28000, "B가방");
    }

    @Test
    void createBrand_ShouldAddNewBrandSuccessfully() {
        // Given
        BrandRequest brandRequest = BrandRequest.builder()
                .name("NewBrand")
                .build();
        Brand newBrand = new Brand(103L, "NewBrand");

        when(brandRepository.save(any(Brand.class))).thenReturn(newBrand);

        // When
        BrandResponse response = adminService.createBrand(brandRequest);

        // Then
        verify(brandRepository, times(1)).save(any(Brand.class));
        assertNotNull(response);
        assertEquals("NewBrand", response.getName());
    }

    @Test
    void createBrand_ShouldThrowExceptionWhenDuplicateBrand() {
        // Given
        String existingBrandName = "BrandAlpha";
        BrandRequest brandRequest = BrandRequest.builder()
                .name(existingBrandName)
                .build();
        when(brandRepository.findByName(existingBrandName)).thenReturn(Optional.of(brandAlpha));

        // Then
        assertThrows(ApplicationException.class, () -> adminService.createBrand(brandRequest));
    }

    @Test
    void updateBrand_ShouldUpdateBrandSuccessfully() {
        // Given
        Long brandId = 101L;
        BrandRequest brandRequest = BrandRequest.builder()
                .name("UpdatedBrandAlpha")
                .build();
        Brand updatedBrand = new Brand(101L, "UpdatedBrandAlpha");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandAlpha));
        when(brandRepository.save(any(Brand.class))).thenReturn(updatedBrand);

        // When
        BrandResponse response = adminService.updateBrand(brandRequest, brandId);

        // Then
        verify(brandRepository, times(1)).findById(brandId);
        verify(brandRepository, times(1)).save(any(Brand.class));
        assertEquals("UpdatedBrandAlpha", response.getName());
    }

    @Test
    void updateBrand_ShouldThrowExceptionWhenBrandNotFound() {
        // Given
        Long nonExistentBrandId = 999L;
        BrandRequest brandRequest = BrandRequest.builder()
                .name("NonExistentBrand")
                .build();

        when(brandRepository.findById(nonExistentBrandId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApplicationException.class, () -> adminService.updateBrand(brandRequest, nonExistentBrandId));
    }

    @Test
    void deleteBrand_ShouldDeleteBrandSuccessfully() {
        // Given
        Long brandId = 101L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandAlpha));

        // When
        BrandResponse response = adminService.deleteBrand(brandId);

        // Then
        verify(brandRepository, times(1)).findById(brandId);
        assertNotNull(response);
        assertEquals("BrandAlpha", response.getName());
    }

    @Test
    void deleteBrand_ShouldThrowExceptionWhenBrandNotFound() {
        // Given
        Long nonExistentBrandId = 999L;

        when(brandRepository.findById(nonExistentBrandId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ApplicationException.class, () -> adminService.deleteBrand(nonExistentBrandId));
    }

    @Test
    void createProduct_ShouldAddProductSuccessfully() {
        // Given
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(201L)
                .brandId(101L)
                .name("NewProduct")
                .price(15000)
                .build();

        Product newProduct = new Product(305L, brandAlpha, categoryShoes, 15000, "NewProduct");

        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brandAlpha));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(categoryShoes));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // When
        ProductResponse response = adminService.createProduct(productRequest);

        // Then
        assertNotNull(response);
        assertEquals("NewProduct", response.getName());
        assertEquals(15000, response.getPrice());
    }

    @Test
    void createProduct_ShouldThrowExceptionWhenBrandOrCategoryNotFound() {
        // Given
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(999L)
                .brandId(101L)
                .name("InvalidProduct")
                .price(10000)
                .build();

        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.empty());

        // Then
        assertThrows(ApplicationException.class, () -> adminService.createProduct(productRequest));
    }

    @Test
    void updateProduct_ShouldUpdateProductSuccessfully() {
        // Given
        Long productId = 301L;
        ProductRequest productRequest = ProductRequest.builder()
                .categoryId(201L)
                .brandId(101L)
                .name("UpdatedProduct")
                .price(18000)
                .build();

        Product updatedProduct = new Product(productId, brandAlpha, categoryShoes, 18000, "UpdatedProduct");

        when(productRepository.findById(productId)).thenReturn(Optional.of(productAlphaShoes));
        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brandAlpha));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(categoryShoes));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ProductResponse response = adminService.updateProduct(productRequest, productId);

        // Then
        assertNotNull(response);
        assertEquals("UpdatedProduct", response.getName());
        assertEquals(18000, response.getPrice());
    }

    @Test
    void deleteProduct_ShouldDeleteProductSuccessfully() {
        // Given
        Long productId = 301L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(productAlphaShoes));

        // When
        ProductResponse response = adminService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        assertNotNull(response);
        assertEquals("A신발", response.getName());
    }
}
