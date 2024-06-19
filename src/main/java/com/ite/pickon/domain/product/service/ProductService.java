package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;

import com.ite.pickon.response.ListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ListResponse findProductList(int storeId, Pageable pageable, String keyword, int totalPage);
    boolean addProduct(ProductRequest productRequest);

    List<ProductResponse> getDetail(String productId);

    List<ProductListVO> getList(Pageable pageable, String keyword);

    int getTotalPage(int storeId, String keyword, int pageSize);
}


