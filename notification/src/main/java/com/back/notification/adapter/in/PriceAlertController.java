package com.back.notification.adapter.in;

import com.back.notification.app.PriceAlertFacade;
import com.back.notification.dto.request.PriceAlertSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/price-alerts")
@RequiredArgsConstructor
public class PriceAlertController {
    private final PriceAlertFacade priceAlertFacade;

    @PostMapping
    public void savePriceAlert(@RequestBody PriceAlertSaveRequestDto dto) {
        Long userId = 1L;   // Todo: 로그인한 사용자로 수정
        priceAlertFacade.save(dto, userId);
    }
}
