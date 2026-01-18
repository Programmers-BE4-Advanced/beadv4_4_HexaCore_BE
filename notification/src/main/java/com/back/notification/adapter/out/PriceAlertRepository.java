package com.back.notification.adapter.out;

import com.back.notification.domain.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
}
