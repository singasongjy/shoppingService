package service.customer;

import me.songjiyoung.dto.customer.response.LowestHighestPriceByCategory;
import me.songjiyoung.dto.customer.response.LowestPriceByBrand;
import me.songjiyoung.dto.customer.response.LowestPriceByCategory;
import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import me.songjiyoung.exception.ApplicationException;
import me.songjiyoung.exception.ErrorMessage;
import me.songjiyoung.repository.BrandRepository;
import me.songjiyoung.repository.CategoryRepository;
import me.songjiyoung.repository.ProductRepository;
import me.songjiyoung.service.customer.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Brand brandAlpha, brandBeta;
    private Category categoryTop, categoryBottom;
    private Product productAlphaTop, productAlphaBottom, productBetaTop, productBetaBottom;

    @BeforeEach
    void setUpTestData() {
        brandAlpha = new Brand(101L, "Alpha");
        brandBeta = new Brand(102L, "Beta");
        categoryTop = new Category(201L, "Top");
        categoryBottom = new Category(202L, "Bottom");
        productAlphaTop = new Product(301L, brandAlpha, categoryTop, 12000, "Alpha Top Product");
        productAlphaBottom = new Product(302L, brandAlpha, categoryBottom, 22000, "Alpha Bottom Product");
        productBetaTop = new Product(303L, brandBeta, categoryTop, 18000, "Beta Top Product");
        productBetaBottom = new Product(304L, brandBeta, categoryBottom, 28000, "Beta Bottom Product");
    }

    /**
     * 테스트: 카테고리별 최저가 상품 조회
     */
    @Test
    void shouldRetrieveLowestPriceByCategory() {
        when(categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(Arrays.asList(categoryTop, categoryBottom));
        when(productRepository.findTopByCategoryOrderByPriceAsc(categoryTop)).thenReturn(productAlphaTop);
        when(productRepository.findTopByCategoryOrderByPriceAsc(categoryBottom)).thenReturn(productAlphaBottom);

        LowestPriceByCategory result = customerService.getLowestPriceByCategory();

        assertNotNull(result);
        assertEquals(2, result.getCategoryPrices().size()); // 카테고리 개수 확인
        assertEquals(34000, result.getTotalPrice()); // 총합 검증
        assertEquals("Alpha", result.getCategoryPrices().get(0).getBrand()); // 첫 번째 카테고리 브랜드
        assertEquals(12000, result.getCategoryPrices().get(0).getPrice()); // 첫 번째 카테고리 가격
        assertEquals("Alpha", result.getCategoryPrices().get(1).getBrand()); // 두 번째 카테고리 브랜드
        assertEquals(22000, result.getCategoryPrices().get(1).getPrice()); // 두 번째 카테고리 가격
    }

    @Test
    void shouldThrowExceptionWhenNoCategoriesFound() {
        when(categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(Collections.emptyList());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> customerService.getLowestPriceByCategory());
        assertEquals(ErrorMessage.CATEGORY_NOT_FOUND, exception.getErrorMessageType());
    }

    @Test
    void shouldThrowExceptionWhenNoProductsFoundForCategories() {
        when(categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(Arrays.asList(categoryTop, categoryBottom));

        when(productRepository.findTopByCategoryOrderByPriceAsc(any(Category.class)))
                .thenReturn(null);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> customerService.getLowestPriceByCategory());
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getErrorMessageType());
    }

    /**
     * 전체 브랜드 중 최저가 브랜드 조회
     */
    @Test
    void shouldRetrieveLowestPriceBrand() {
        when(brandRepository.findAll()).thenReturn(Arrays.asList(brandAlpha, brandBeta));

        LowestPriceByBrand result = customerService.getLowestPriceByBrand();

        assertNotNull(result);
        assertEquals("Alpha", result.getLowestPrice().getBrand());
        assertEquals(0, result.getLowestPrice().getTotalPrice());
        assertEquals(0, result.getLowestPrice().getCategories().size());
    }

    @Test
    void shouldThrowExceptionWhenNoBrandsFound() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ApplicationException.class, () -> customerService.getLowestPriceByBrand());
    }

    /**
     * 특정 카테고리에서 최저가 및 최고가 상품 조회
     */
    @Test
    void shouldRetrievePriceRangeForCategory() {
        when(categoryRepository.findByName("Top")).thenReturn(Optional.of(categoryTop));
        when(productRepository.findTopByCategoryOrderByPriceAsc(categoryTop)).thenReturn(productAlphaTop);
        when(productRepository.findTopByCategoryOrderByPriceDesc(categoryTop)).thenReturn(productBetaTop);

        LowestHighestPriceByCategory result = customerService.getLowestHighestPriceByCategory("Top");

        assertNotNull(result);
        assertEquals("Top", result.getCategory());
        assertEquals("Alpha", result.getLowestPrice().getBrand());
        assertEquals(12000, result.getLowestPrice().getPrice());
        assertEquals("Beta", result.getHighestPrice().getBrand());
        assertEquals(18000, result.getHighestPrice().getPrice());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findByName("NonExistentCategory")).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> customerService.getLowestHighestPriceByCategory("NonExistentCategory"));
    }
}
