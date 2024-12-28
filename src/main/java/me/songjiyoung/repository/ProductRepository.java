package me.songjiyoung.repository;

import me.songjiyoung.entity.Brand;
import me.songjiyoung.entity.Category;
import me.songjiyoung.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findTopByCategoryOrderByPriceAsc(Category category);
    Product findTopByCategoryOrderByPriceDesc(Category category);
    List<Product> findByBrand(Brand brand);
    Product findTopByBrandAndCategoryOrderByPriceAsc(Brand brand, Category category);
    boolean existsByBrandId(Long brandId);
}