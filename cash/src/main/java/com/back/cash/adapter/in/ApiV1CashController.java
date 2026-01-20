package com.back.cash.adapter.in;

import com.back.cash.app.CashFacade;
import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cash")
@RequiredArgsConstructor
public class ApiV1CashController {
    private final CashFacade cashFacade;

    @PostMapping("/payments")
    public PayAndHoldResponseDto payAndHold(@RequestBody PayAndHoldRequestDto dto) {
        return cashFacade.payAndHold(dto);
    }
}
