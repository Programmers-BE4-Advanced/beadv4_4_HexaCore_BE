package com.back.notification.adapter.in;

import com.back.common.response.CommonResponse;
import com.back.notification.app.NotificationFacade;
import com.back.notification.dto.NotificationIdResponseDto;
import com.back.security.principal.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class ApiV1NotificationController implements NotificationController {

    private final NotificationFacade notificationFacade;

    @Override
    @PatchMapping("/{notificationId}")
    public CommonResponse<NotificationIdResponseDto> markAsRead(
            @AuthenticationPrincipal AuthPrincipal authPrincipal,
            @PathVariable String notificationId
    ) {
        NotificationIdResponseDto response =
                notificationFacade.markUserNotificationAsRead(authPrincipal.getUserId(), notificationId);

        return CommonResponse.successWithData(HttpStatus.OK, response);
    }
}
