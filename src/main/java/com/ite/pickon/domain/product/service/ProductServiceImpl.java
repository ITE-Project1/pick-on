package com.ite.pickon.domain.product.service;

import com.ite.pickon.domain.product.dto.*;
import com.ite.pickon.domain.product.mapper.ProductMapper;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
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
    public ListResponse findProductList(int storeId, Pageable pageable, String keyword, int totalPage) {
        List<ProductAdminVO> productAdminList = productMapper.selectProductListByStore(storeId, pageable, keyword);
        if (productAdminList.size() == 0) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        return new ListResponse(productAdminList, totalPage);
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

    public ListResponse getList(Pageable pageable, String keyword, int totalPage){

        List<ProductListVO> productList = productMapper.selectProductList(pageable, keyword);

        if (productList.size() == 0) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        return new ListResponse(productList, totalPage);
    }

    @Override
    public int getTotalPage(int storeId, String keyword, int pageSize) {
        return productMapper.countTotalProductPages(storeId, keyword, pageSize);
    }
    @Override
    public int getTotalProductPage(String keyword, int pageSize){
        return productMapper.countTotalBasicProductPages(keyword, pageSize);
    }

    // 상품 ID 생성
    private String generateProductId(String brandInitial) {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return brandInitial + uuidPart;
    }
}
