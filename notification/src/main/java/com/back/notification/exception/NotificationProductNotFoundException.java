package com.back.notification.exception;

import com.back.common.code.FailureCode;
import com.back.common.exception.CustomException;

public class NotificationProductNotFoundException extends CustomException {
    public NotificationProductNotFoundException(){
        super("NotificationProduct가 존재하지 않습니다.", FailureCode.ENTITY_NOT_FOUND);
    }
}
