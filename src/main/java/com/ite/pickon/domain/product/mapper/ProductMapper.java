package com.ite.pickon.domain.product.mapper;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
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

    ProductResponse selectProductDetail(String productId);
}
