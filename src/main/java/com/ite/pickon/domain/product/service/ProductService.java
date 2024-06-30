package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;

import com.ite.pickon.response.ListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ListResponse findProductList(int storeId, Pageable pageable, String keyword, int totalPage);
    boolean addProduct(ProductVO productVO);

    List<ProductInfo> getDetail(String productId);

    ListResponse getList(Pageable pageable, String keyword, int totalPage);

    int getTotalPage(int storeId, String keyword, int pageSize);

    int getTotalProductPage(String keyword, int pageSize);
}


