package com.ite.pickon.domain.stock.controller;

import com.ite.pickon.domain.stock.dto.StockRequest;
import com.ite.pickon.domain.stock.service.StockService;
import com.ite.pickon.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    /**
     * 재고 등록
     *
     * @param stockRequest 재고 요청 데이터
     * @return 재고 등록 결과 응답
     */
    @PostMapping(value="/stocks", produces = "application/json; charset=UTF-8")
    public ResponseEntity<SimpleResponse> stockAdd(@RequestBody StockRequest stockRequest) {
        stockService.addStock(stockRequest);
        return new ResponseEntity<>(new SimpleResponse("재고 등록이 완료되었습니다."), HttpStatus.OK);
    }
}
