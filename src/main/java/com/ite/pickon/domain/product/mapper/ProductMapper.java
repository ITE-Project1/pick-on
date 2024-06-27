package com.ite.pickon.domain.product.mapper;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.product.dto.ProductListVO;
import com.ite.pickon.domain.product.dto.ProductResponse;
import com.ite.pickon.domain.product.dto.ProductVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductMapper {

    /**
     * 지점별 상품 목록 조회
     *
     * @param storeId 지점 ID
     * @param pageable 페이지 정보
     * @param keyword 검색 키워드
     * @return 특정 지점의 상품 목록
     */
    List<ProductAdminVO> selectProductListByStore(@Param("storeId") int storeId,
                                                  @Param("pageable") Pageable pageable,
                                                  @Param("keyword") String keyword);

    /**
     * 신규 상품 등록
     *
     * @param productVO 상품 정보
     * @return 등록된 행 수
     */
    int insertNewProduct(ProductVO productVO);

    /**
     * 상품 상세 조회
     *
     * @param productId 상품 ID
     * @return 상품 상세 정보 목록
     */
    List<ProductResponse> selectProductDetail(String productId);

    /**
     * 기본 상품 목록 조회
     *
     * @param pageable 페이지 정보
     * @param keyword 검색 키워드
     * @return 기본 상품 목록
     */
    List<ProductListVO> selectProductList(@Param("pageable") Pageable pageable,
                                          @Param("keyword") String keyword);

    /**
     * 브랜드 약어 조회
     *
     * @param brandId 브랜드 ID
     * @return 브랜드 약어
     */
    String selectBrandInitialByBrandId(int brandId);

    /**
     * 지점별 상품 목록의 전체 페이지 수 계산
     *
     * @param storeId 지점 ID
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    int countTotalProductPages(@Param("storeId") int storeId,
                               @Param("keyword") String keyword,
                               @Param("pageSize") int pageSize);

    /**
     * 기본 상품 목록의 전체 페이지 수 계산
     *
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    int countTotalBasicProductPages(@Param("keyword") String keyword,
                                    @Param("pageSize") int pageSize);
}
