package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;
import com.ite.pickon.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    @Override
    public List<ProductAdminVO> findProductList(String storeId, Pageable pageable, String keyword) {
        return productMapper.selectProductListByStore(storeId, pageable, keyword);
    }

    @Transactional
    public boolean addProduct(ProductRequest productRequest) {
        String brandInitial = productMapper.selectBrandInitialByBrandId(productRequest.getBrandId());
        String productId = generateProductId(brandInitial);
        int updateCnt = productMapper.insertNewProduct(ProductVO.of(productRequest, productId));
        return updateCnt == 1;
    }

    @Override
    public List<ProductResponse> getDetail(String productId) {
        List<ProductResponse> result = productMapper.selectProductDetail(productId);
        return result;
    }

    public List<ProductListVO> getList(Pageable pageable, String keyword){
        return productMapper.selectProductList(pageable, keyword);
    }

    // 상품 ID 생성
    private String generateProductId(String brandInitial) {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return brandInitial + uuidPart;
    }
}
