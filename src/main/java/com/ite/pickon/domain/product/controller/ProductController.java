package com.ite.pickon.domain.product.controller;

import com.ite.pickon.domain.product.dto.ProductListVO;
import com.ite.pickon.domain.product.dto.ProductRequest;
import com.ite.pickon.domain.product.dto.ProductResponse;
import com.ite.pickon.domain.product.service.ProductService;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private static final int PRODUCTS_PAGE_SIZE = 10;
    private final ProductService productService;

    // 지점별 상품 목록 조회 (관리자)
    @GetMapping("/admin/products")
    public ResponseEntity<ListResponse> getProductList(@RequestParam int storeId,
                                                       @RequestParam int page,
                                                       @RequestParam(required = false) String sort,
                                                       @RequestParam(required = false) String keyword) {

        int totalPage = productService.getTotalPage(storeId, keyword, PRODUCTS_PAGE_SIZE);

        // 전체 페이지 개수를 넘는 요청을 보내면 예외 처리
        if (page >= totalPage) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        Sort sortOrder = Sort.by("created_at").descending();
        if ("priceHigh".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        }
        if ("priceLow".equals(sort)) {
            sortOrder = Sort.by("price").ascending();
        }

        Pageable pageable = PageRequest.of(page, 10, sortOrder);
        return ResponseEntity.ok(productService.findProductList(storeId, pageable, keyword, totalPage));
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
        return ResponseEntity.ok(productService.getDetail(productId));
    }

    @GetMapping("/products/list")
    public ResponseEntity<ListResponse> getBasicProductList(@RequestParam int page,
                                                                   @RequestParam String sort,
                                                                   @RequestParam(required = false) String keyword) {
        // 전체 페이지 개수 조회(0부터 시작이라 mapper에서 CEIL로 했음)
        int totalPage = productService.getTotalProductPage(keyword, PRODUCTS_PAGE_SIZE);

        // 전체 페이지 개수를 넘는 요청을 보내면 예외 처리
        if (page > totalPage) {
            throw new CustomException(ErrorCode.FIND_FAIL_PRODUCTS);
        }

        Sort sortOrder = Sort.by("created_at").descending(); //최신순
        if ("priceHigh".equals(sort)) {   //낮은가격순
            sortOrder = Sort.by("price").descending();
        }
        if ("priceLow".equals(sort)) {   //높은가격순
            sortOrder = Sort.by("price").ascending();
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, 10, sortOrder);

        return ResponseEntity.ok(productService.getList(pageable, keyword, totalPage));
    }

}
