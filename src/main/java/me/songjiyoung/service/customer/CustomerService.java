package me.songjiyoung.service.customer;

import me.songjiyoung.dto.customer.response.LowestHighestPriceByCategory;
import me.songjiyoung.dto.customer.response.LowestPriceByBrand;
import me.songjiyoung.dto.customer.response.LowestPriceByCategory;

public interface CustomerService {
    LowestPriceByCategory getLowestPriceByCategory();
    LowestPriceByBrand getLowestPriceByBrand();
    LowestHighestPriceByCategory getLowestHighestPriceByCategory(String categoryName);
}