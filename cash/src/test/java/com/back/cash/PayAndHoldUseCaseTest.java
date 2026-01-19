package com.back.cash;

import com.back.cash.adapter.out.PaymentRepository;
import com.back.cash.app.CashLogSupport;
import com.back.cash.app.PayAndHoldUseCase;
import com.back.cash.app.WalletSupport;
import com.back.cash.domain.Payment;
import com.back.cash.domain.Wallet;
import com.back.cash.domain.enums.PayAndHoldStatus;
import com.back.cash.domain.enums.PaymentStatus;
import com.back.cash.domain.enums.RelType;
import com.back.cash.domain.enums.WalletType;
import com.back.cash.dto.request.PayAndHoldRequestDto;
import com.back.cash.dto.response.PayAndHoldResponseDto;
import com.back.cash.mapper.PaymentMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayAndHoldUseCaseTest {

    @InjectMocks
    private PayAndHoldUseCase payAndHoldUseCase;

    @Mock
    private WalletSupport walletSupport;
    @Mock
    private CashLogSupport cashLogSupport;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제 요청 시 예치금이 충분하여 예치금 차감 결제인 경우 지갑 이동과 로그 기록이 호출된다")
    void execute_Success() {
        // given
        Long buyerId = 1L;
        BigDecimal amount = new BigDecimal("5000");
        PayAndHoldRequestDto dto = new PayAndHoldRequestDto(RelType.ORDER, 100L, buyerId, amount, "나이키 운동화");

        Payment payment = PaymentMapper.toPayment(dto);

        Wallet buyerWallet = spy(Wallet.builder()
                .userId(buyerId)
                .balance(amount)
                .walletType(WalletType.USER)
                .build());

        Wallet systemWallet = spy(Wallet.builder()
                .userId(null)
                .balance(BigDecimal.ZERO)
                .walletType(WalletType.SYSTEM)
                .build());

        given(paymentRepository.findByRelTypeAndRelId(any(), any())).willReturn(Optional.empty());
        given(paymentRepository.save(any(Payment.class))).willReturn(payment);

        given(walletSupport.getUserWallet(buyerId)).willReturn(buyerWallet);
        given(walletSupport.getSystemWallet()).willReturn(systemWallet);

        // when
        PayAndHoldResponseDto result = payAndHoldUseCase.execute(dto);

        // Then: withdraw, deposit 메서드가 호출되었는가? (엔티티 상태 변경 확인)
        assertThat(buyerWallet.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(systemWallet.getBalance()).isEqualByComparingTo(amount);

        assertThat(result.walletUsedAmount()).isEqualByComparingTo(amount);
        assertThat(result.status()).isEqualTo(PayAndHoldStatus.PAID);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);

        verify(cashLogSupport, times(1)).recordHoldingLog(
                eq(buyerWallet),
                eq(systemWallet),
                eq(amount),
                eq(RelType.ORDER),
                eq(100L)
        );

    }

}
