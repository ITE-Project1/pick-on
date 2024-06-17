package com.ite.pickon.domain.product.mapper;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.product.dto.ProductListVO;
import com.ite.pickon.domain.product.dto.ProductResponse;
import com.ite.pickon.domain.product.dto.ProductVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ProductMapper {
    List<ProductAdminVO> selectProductListByStore(@Param("storeId") String storeId,
                                                  @Param("pageable") Pageable pageable,
                                                  @Param("keyword") String keyword);

    int insertNewProduct(ProductVO productVO);

    List<ProductResponse> selectProductDetail(String productId);

    List<ProductListVO> selectProductList(@Param("pageable") Pageable pageable,
                                          @Param("keyword") String keyword);
    //@Param: MyBatis가 메서드의 파라미터 이름을 SQL 문 내에 맵핑하기 위해 쓴다
}
