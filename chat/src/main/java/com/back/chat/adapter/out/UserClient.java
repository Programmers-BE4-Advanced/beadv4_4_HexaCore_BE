package com.back.chat.adapter.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userClient", url = "${user-service.url}")
public interface UserClient {

    @PostMapping("/internal/users/{userId}/blind-count:increment")
    void incrementBlindCount(
            @PathVariable("userId") Long userId,
            @RequestParam("messageId") Long messageId
    );
}
