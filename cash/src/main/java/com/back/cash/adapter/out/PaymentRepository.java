package com.back.cash.adapter.out;

import com.back.cash.domain.Payment;
import com.back.cash.domain.enums.RelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRelTypeAndRelId(RelType relType, Long relId);
}
