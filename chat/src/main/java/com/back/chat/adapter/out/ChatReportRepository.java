package com.back.chat.adapter.out;

import com.back.chat.domain.ChatReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReportRepository extends JpaRepository<ChatReport,Long> {
}
