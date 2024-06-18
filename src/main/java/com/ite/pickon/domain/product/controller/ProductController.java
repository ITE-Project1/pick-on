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

        //service단에서 throws 하기
        return null;
    }

    //상품 상세조회
    @GetMapping("/products/detail/{productId}")
    public ResponseEntity<List<ProductResponse>> getProductDetail(@PathVariable String productId) {
        //ResponseEntity는 HTTP 응답을 나타내는 클래스로서, 클라이언트에게 반환할 데이터와 HTTP 상태 코드를 포함
        List<ProductResponse> response = productService.getDetail(productId);
        // ProductResponse 객체 로깅

        // ResponseEntity 생성 후 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
        //return new ResponseEntity<>(productService.getDetail(productId),HttpStatus.OK);
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
        Pageable pageable = PageRequest.of(page, 10, sortOrder); //페이지 번호(page)를 기준으로 한 페이지에 10개의 항목을 포함하며, sortOrder에 지정된 정렬 순서대로 데이터를 가져오는 PageRequest 객체
        List<ProductListVO> response = productService.getList(pageable, keyword);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
