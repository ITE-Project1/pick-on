package com.ite.pickon.domain.product.controller;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 지점별 상품 목록 조회 (관리자)
    @GetMapping("/admin")
    public ResponseEntity<List<ProductAdminVO>> getProductList(@RequestParam String storeId,
                                                               @RequestParam int page,
                                                               @RequestParam String sort,
                                                               @RequestParam(required = false) String keyword) {

        Sort sortOrder = Sort.by("created_at").descending();
        if ("priceHigh".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        }
        if ("priceLow".equals(sort)) {
            sortOrder = Sort.by("price").ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        return new ResponseEntity<>(productService.findProductList(storeId, pageable, keyword), HttpStatus.OK);
    }

}
