package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;
import com.ite.pickon.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public List<ProductAdminVO> findProductList(String storeId, Pageable pageable, String keyword) {
        return productMapper.selectProductListByStore(storeId, pageable, keyword);
    }

    public int insertProduct(ProductVO productVO){
//        ProductVO productVO = ProductVO.of(productRequest);
        return productMapper.insertNewProduct(productVO);

    }

    @Override
    public List<ProductResponse> getDetail(String productId) {
        System.out.println("getting detail for id : "+ productId);
        List<ProductResponse> result = productMapper.selectProductDetail(productId);
        System.out.println("Product detail retrieved: {} "+ result);
        return result;
    }

    public List<ProductListVO> getList(Pageable pageable, String keyword){
        return productMapper.selectProductList(pageable, keyword);
    }
}
