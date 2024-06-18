package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ProductAdminVO> findProductList(String storeId, Pageable pageable, String keyword);
    boolean addProduct(ProductRequest productRequest);

    List<ProductResponse> getDetail(String productId);

    List<ProductListVO> getList(Pageable pageable, String keyword);
}


