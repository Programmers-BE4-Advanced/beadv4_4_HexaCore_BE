package com.back.notification.exception;

import com.back.common.code.FailureCode;
import com.back.common.exception.CustomException;

public class NotificationAccessDeniedException extends CustomException {
    public NotificationAccessDeniedException(){
        super("해당 알림에 접근할 수 없습니다.", FailureCode.FORBIDDEN);
    }
}
