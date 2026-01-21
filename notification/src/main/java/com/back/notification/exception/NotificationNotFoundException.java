package com.back.notification.exception;

import com.back.common.code.FailureCode;
import com.back.common.exception.CustomException;

public class NotificationNotFoundException extends CustomException {
    public NotificationNotFoundException(){
        super("존재하지 않는 알림입니다.", FailureCode.ENTITY_NOT_FOUND);
    }
}
