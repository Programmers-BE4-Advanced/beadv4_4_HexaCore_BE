package com.back.notification.adapter.in;

import com.back.common.code.SuccessCode;
import com.back.common.response.CommonResponse;
import com.back.notification.app.NotificationFacade;
import com.back.notification.dto.NotificationIdResponseDto;
import com.back.notification.dto.response.NotificationListResponseDto;
import com.back.security.principal.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class ApiV1NotificationController implements NotificationController {

    private final NotificationFacade notificationFacade;

    @GetMapping()
    @Override
    public CommonResponse<NotificationListResponseDto> getNotifications(
            @AuthenticationPrincipal AuthPrincipal authPrincipal,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        NotificationListResponseDto response = notificationFacade.getRecentNotifications(
                authPrincipal.getUserId(), pageNumber, pageSize);

        return CommonResponse.success(SuccessCode.OK, response);
    }

    @Override
    @PatchMapping("/{notificationId}")
    public CommonResponse<NotificationIdResponseDto> markAsRead(
            @AuthenticationPrincipal AuthPrincipal authPrincipal,
            @PathVariable String notificationId
    ) {
        NotificationIdResponseDto response =
                notificationFacade.markUserNotificationAsRead(authPrincipal.getUserId(), notificationId);

        return CommonResponse.success(SuccessCode.OK, response);
    }
}
