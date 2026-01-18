package com.back.notification.app;

import com.back.notification.dto.request.PriceAlertSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAlertFacade {
    private final PriceAlertSaveUsecase priceAlertSaveUsecase;

    public void save(PriceAlertSaveRequestDto dto, Long userId) {
        priceAlertSaveUsecase.save(dto, userId);
    }
}
