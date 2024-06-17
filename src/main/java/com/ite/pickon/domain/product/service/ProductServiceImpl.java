package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.product.dto.ProductRequest;
import com.ite.pickon.domain.product.dto.ProductResponse;
import com.ite.pickon.domain.product.dto.ProductVO;
import com.ite.pickon.domain.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    @Override
    public List<ProductAdminVO> findProductList(String storeId, Pageable pageable, String keyword) {
        return productMapper.selectProductListByStore(storeId, pageable, keyword);
    }

    public int insertProduct(ProductRequest productRequest){
        ProductVO productVO = ProductVO.of(productRequest);
        return productMapper.insertNewProduct(productVO);

    }

    @Override
    public ProductResponse getDetail(String productId) {
        return productMapper.selectProductDetail(productId);
    }
}
