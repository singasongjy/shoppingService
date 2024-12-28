package me.songjiyoung.service.customer;

import me.songjiyoung.dto.customer.response.LowestHighestPriceByCategory;
import me.songjiyoung.dto.customer.response.LowestPriceByBrand;
import me.songjiyoung.dto.customer.response.LowestPriceByCategory;
import me.songjiyoung.exception.ApplicationException;
import me.songjiyoung.exception.ErrorMessage;
import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import me.songjiyoung.repository.BrandRepository;
import me.songjiyoung.repository.CategoryRepository;
import me.songjiyoung.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public LowestPriceByCategory getLowestPriceByCategory() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        if (categories.isEmpty()) {
            throw new ApplicationException(ErrorMessage.CATEGORY_NOT_FOUND);
        }

        LowestPriceByCategory result = new LowestPriceByCategory();
        List<LowestPriceByCategory.CategoryPrice> categoryPrices = new ArrayList<>();
        int totalPrice = 0;

        for (Category category : categories) {
            Product lowestPriceProduct = productRepository.findTopByCategoryOrderByPriceAsc(category);

            if (lowestPriceProduct != null) {
                categoryPrices.add(
                        new LowestPriceByCategory.CategoryPrice(
                                category.getName(),
                                lowestPriceProduct.getBrand().getName(),
                                lowestPriceProduct.getPrice()
                        )
                );
                totalPrice += lowestPriceProduct.getPrice();
            }
        }

        if (categoryPrices.isEmpty()) {
            throw new ApplicationException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        result.setCategoryPrices(categoryPrices);
        result.setTotalPrice(totalPrice);
        return result;
    }

    public LowestPriceByBrand getLowestPriceByBrand() {

        List<Brand> brands = brandRepository.findAll();
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        if (brands.isEmpty()) {
            throw new ApplicationException(ErrorMessage.BRAND_NOT_FOUND);
        }

        Brand lowestPriceBrand = null;
        int lowestTotalPrice = Integer.MAX_VALUE;
        Map<String, Product> lowestPriceProducts = null;

        for (Brand brand : brands) {
            Map<String, Product> categoryToLowestProduct = new HashMap<>();
            int totalPrice = 0;

            boolean hasAllCategories = true;

            for (Category category : categories) {
                Product lowestProduct = productRepository.findTopByBrandAndCategoryOrderByPriceAsc(brand, category);
                if (lowestProduct == null) {
                    hasAllCategories = false;
                    break;
                }
                categoryToLowestProduct.put(category.getName(), lowestProduct);
                totalPrice += lowestProduct.getPrice();
            }

            if (!hasAllCategories) {
                continue;
            }

            if (totalPrice < lowestTotalPrice) {
                lowestTotalPrice = totalPrice;
                lowestPriceBrand = brand;
                lowestPriceProducts = categoryToLowestProduct;
            }
        }

        if (lowestPriceBrand == null) {
            throw new ApplicationException(ErrorMessage.NO_BRAND_WITH_PRODUCTS_FOR_ALL_CATEGORIES);
        }

        return buildLowestPriceByBrand(lowestPriceBrand, lowestTotalPrice, lowestPriceProducts);
    }

    private LowestPriceByBrand buildLowestPriceByBrand(
            Brand brand, int totalPrice, Map<String, Product> productsByCategory
    ) {
        LowestPriceByBrand result = new LowestPriceByBrand();
        LowestPriceByBrand.LowestPrice lowestPrice = new LowestPriceByBrand.LowestPrice();

        lowestPrice.setBrand(brand.getName());
        lowestPrice.setTotalPrice(totalPrice);

        List<LowestPriceByBrand.CategoryPrice> categoryPrices = productsByCategory.values().stream()
                .sorted(Comparator.comparing(product -> product.getCategory().getId()))
                .map(product -> new LowestPriceByBrand.CategoryPrice(
                        product.getCategory().getName(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());

        lowestPrice.setCategories(categoryPrices);
        result.setLowestPrice(lowestPrice);
        return result;
    }

    public LowestHighestPriceByCategory getLowestHighestPriceByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.CATEGORY_NOT_FOUND, categoryName));

        Product lowestPriceProduct = productRepository.findTopByCategoryOrderByPriceAsc(category);
        Product highestPriceProduct = productRepository.findTopByCategoryOrderByPriceDesc(category);

        if (lowestPriceProduct == null && highestPriceProduct == null) {
            throw new ApplicationException(ErrorMessage.PRODUCT_NOT_FOUND_FOR_CATEGORY, categoryName);
        }

        return buildLowestHighestPriceByCategory(categoryName, lowestPriceProduct, highestPriceProduct);
    }

    private LowestHighestPriceByCategory buildLowestHighestPriceByCategory(
            String categoryName, Product lowestPriceProduct, Product highestPriceProduct) {

        LowestHighestPriceByCategory result = new LowestHighestPriceByCategory();
        result.setCategory(categoryName);

        if (lowestPriceProduct != null) {
            LowestHighestPriceByCategory.BrandPrice lowestPrice = new LowestHighestPriceByCategory.BrandPrice();
            lowestPrice.setBrand(lowestPriceProduct.getBrand().getName());
            lowestPrice.setPrice(lowestPriceProduct.getPrice());
            result.setLowestPrice(lowestPrice);
        }

        if (highestPriceProduct != null) {
            LowestHighestPriceByCategory.BrandPrice highestPrice = new LowestHighestPriceByCategory.BrandPrice();
            highestPrice.setBrand(highestPriceProduct.getBrand().getName());
            highestPrice.setPrice(highestPriceProduct.getPrice());
            result.setHighestPrice(highestPrice);
        }

        return result;
    }
}
