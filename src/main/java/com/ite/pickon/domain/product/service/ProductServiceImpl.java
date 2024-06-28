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

    /**
     * 지점별 상품 목록 조회
     *
     * @param storeId 지점 ID
     * @param pageable 페이지 정보
     * @param keyword 검색 키워드
     * @param totalPage 전체 페이지 수
     * @return 상품 목록 및 전체 페이지 수를 포함한 응답 객체
     */
    @Override
    public ListResponse findProductList(int storeId, Pageable pageable, String keyword, int totalPage) {
        List<ProductAdminVO> productAdminList = productMapper.selectProductListByStore(storeId, pageable, keyword);
        if (productAdminList.size() == 0) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        return new ListResponse(productAdminList, totalPage);
    }

    /**
     * 상품 등록
     *
     * @param productRequest 상품 요청 DTO
     * @return 상품 등록 성공 여부
     */
    @Transactional
    public boolean addProduct(ProductRequest productRequest) {
        String brandInitial = productMapper.selectBrandInitialByBrandId(productRequest.getBrandId());
        String productId = generateProductId(brandInitial);
        int updateCnt = productMapper.insertNewProduct(ProductVO.of(productRequest, productId));
        return updateCnt == 1;
    }

    /**
     * 상품 상세 조회
     *
     * @param productId 상품 ID
     * @return 상품 상세 정보 리스트
     */
    @Override
    public List<ProductResponse> getDetail(String productId) {
        List<ProductResponse> result = productMapper.selectProductDetail(productId);
        return result;
    }

    /**
     * 기본 상품 목록 조회
     *
     * @param pageable 페이지 정보
     * @param keyword 검색 키워드
     * @param totalPage 전체 페이지 수
     * @return 상품 목록 및 전체 페이지 수를 포함한 응답 객체
     */
    public ListResponse getList(Pageable pageable, String keyword, int totalPage){
        List<ProductListVO> productList = productMapper.selectProductList(pageable, keyword);
        if (productList.size() == 0) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        return new ListResponse(productList, totalPage);
    }

    /**
     * 지점별 상품 목록의 전체 페이지 수 계산
     *
     * @param storeId 지점 ID
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    @Override
    public int getTotalPage(int storeId, String keyword, int pageSize) {
        return productMapper.countTotalProductPages(storeId, keyword, pageSize);
    }

    /**
     * 기본 상품 목록의 전체 페이지 수 계산
     *
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    @Override
    public int getTotalProductPage(String keyword, int pageSize){
        return productMapper.countTotalBasicProductPages(keyword, pageSize);
    }

    /**
     * 상품 ID 생성
     *
     * @param brandInitial 브랜드 약어
     * @return 생성된 상품 ID
     */
    private String generateProductId(String brandInitial) {
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return brandInitial + uuidPart;
    }
}
