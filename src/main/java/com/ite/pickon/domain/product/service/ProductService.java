package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductAdminVO> findProductList(String storeId, Pageable pageable, String keyword);
    int insertProduct(ProductRequest productRequest);

    ProductResponse getDetail(String productId);
}


