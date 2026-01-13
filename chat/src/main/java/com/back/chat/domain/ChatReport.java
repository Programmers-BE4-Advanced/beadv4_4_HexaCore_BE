package com.back.chat.domain;

import com.back.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChatReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;

    @Column(name = "reporter_member_id")
    private Long reporterMemberId;

    @Column(name = "reported_member_id")
    private Long reportedMemberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private ChatReportReason reason;


}
