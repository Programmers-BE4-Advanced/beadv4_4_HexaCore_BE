package com.back.cash.app;

import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashFacade {

    private final PayAndHoldUseCase payAndHoldUseCase;

    @Transactional
    public PayAndHoldResponseDto payAndHold(PayAndHoldRequestDto dto) {
        return payAndHoldUseCase.execute(dto);
    }
}
