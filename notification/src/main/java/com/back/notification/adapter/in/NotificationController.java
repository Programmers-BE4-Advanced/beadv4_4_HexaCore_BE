package com.back.notification.adapter.in;

import com.back.common.response.CommonResponse;
import com.back.notification.dto.NotificationIdResponseDto;
import com.back.notification.dto.response.NotificationListResponseDto;
import com.back.security.principal.AuthPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notification", description = "알림 관련 API")
public interface NotificationController {

    @Operation(
            summary = "최근 알림 조회",
            description = """
                사용자의 최근 알림 목록을 조회합니다.
                읽음 여부와 관계없이 생성일 기준 최신 순으로 반환됩니다.
                페이징을 위해 `page`(페이지 번호)와 `size`(페이지 크기)를 지원합니다.
                """
    )
    @Parameters({
            @Parameter(
                    name = "page",
                    description = "조회할 페이지 번호 (0부터 시작)",
                    example = "0"
            ),
            @Parameter(
                    name = "size",
                    description = "페이지당 항목 수",
                    example = "20"
            )
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 알림 목록을 반환함")
    })
    CommonResponse<NotificationListResponseDto> getNotifications(
            @AuthenticationPrincipal AuthPrincipal authPrincipal,
            @RequestParam("page") int pageNumber,
            @RequestParam("size") int pageSize);

    @Operation(summary = "알림 읽음 처리", description = "사용자의 특정 알림을 읽음 처리합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 처리 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    @ApiResponse(responseCode = "403", description = "접근 권한 없음", content = @Content)
    @ApiResponse(responseCode = "404", description = "알림 없음", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    CommonResponse<NotificationIdResponseDto> markAsRead(AuthPrincipal principal,
                                                         String notificationId);

}
