package com.back.notification.adapter.in;

import com.back.common.response.CommonResponse;
import com.back.notification.dto.NotificationIdResponseDto;
import com.back.security.principal.AuthPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notification", description = "알림 관련 API")
public interface NotificationController {

    @Operation(summary = "알림 읽음 처리", description = "사용자의 특정 알림을 읽음 처리합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 처리 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "알림 없음", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    CommonResponse<NotificationIdResponseDto> markAsRead(AuthPrincipal principal,
                                                         String notificationId);

}
