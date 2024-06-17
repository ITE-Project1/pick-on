package com.ite.pickon.domain.product.controller;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.product.dto.ProductRequest;
import com.ite.pickon.domain.product.dto.ProductResponse;
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
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 지점별 상품 목록 조회 (관리자)
    @GetMapping("/admin/products")
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
//상품등록
    @PostMapping("/products/register")
    public void createProduct(@RequestBody ProductRequest productRequest){
        if (productService.insertProduct(productRequest)==1){
            System.out.println("insert success"); // 이렇게 처리해도 되나?
        };
        //service단에서 throws 하기
    }
//상품 상세조회
    @GetMapping("/products/detail/{productId}")
    public ResponseEntity<ProductResponse> getProductDetail(@PathVariable String productId){
        System.out.println("====================");
        //return new ResponseEntity<>(productService.getDetail(productId),HttpStatus.OK);
        //ResponseEntity는 HTTP 응답을 나타내는 클래스로서, 클라이언트에게 반환할 데이터와 HTTP 상태 코드를 포함
        ProductResponse response = productService.getDetail(productId);
        // ProductResponse 객체 로깅
        System.out.println(response);

        // ResponseEntity 생성 후 반환
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
