package com.ite.pickon.domain.product.controller;

import com.ite.pickon.domain.product.dto.*;
import com.ite.pickon.domain.product.service.ProductService;
import com.ite.pickon.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 지점별 상품 목록 조회 (관리자)
    @GetMapping("/admin/products")
    public ResponseEntity<List<ProductAdminVO>> getProductList(@RequestParam String storeId,
                                                               @RequestParam int page,
                                                               @RequestParam(required = false) String sort,
                                                               @RequestParam(required = false) String keyword) {

        Sort sortOrder = Sort.by("created_at").descending();
        if ("priceHigh".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        }
        if ("priceLow".equals(sort)) {
            sortOrder = Sort.by("price").ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        return ResponseEntity.ok(productService.findProductList(storeId, pageable, keyword));
    }

    //상품등록
    @PostMapping("/products/register")
    public ResponseEntity<SimpleResponse> createProduct(@RequestBody ProductRequest productRequest) {
        boolean isRegistered = productService.addProduct(productRequest);

        if (isRegistered) {
            return ResponseEntity.ok(new SimpleResponse("상품 등록이 완료되었습니다."));
        }

        return null;
    }

    //상품 상세조회
    @GetMapping("/products/detail/{productId}")
    public ResponseEntity<List<ProductResponse>> getProductDetail(@PathVariable String productId) {
        List<ProductResponse> response = productService.getDetail(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/products/list")
    public ResponseEntity<List<ProductListVO>> getBasicProductList(@RequestParam int page,
                                                                   @RequestParam String sort,
                                                                   @RequestParam(required = false) String keyword) {

        Sort sortOrder = Sort.by("created_at").descending(); //최신순
        if ("priceHigh".equals(sort)) {   //낮은가격순
            sortOrder = Sort.by("price").descending();
        }
        if ("priceLow".equals(sort)) {   //높은가격순
            sortOrder = Sort.by("price").ascending();
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        List<ProductListVO> response = productService.getList(pageable, keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
