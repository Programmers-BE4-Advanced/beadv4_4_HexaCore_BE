package com.back.chat.adapter.in;

import com.back.chat.app.ChatFacade;
import com.back.chat.dto.request.ChatRoomEnterRequestDto;
import com.back.chat.dto.response.ChatRoomEnterResponseDto;
import com.back.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatFacade chatFacade;

    @PostMapping("/enter")
    public CommonResponse<ChatRoomEnterResponseDto> enter(@RequestParam("brandId") Long brandId,
                                                          @AuthenticationPrincipal UserDetails userDetails){
        // Long userId = customUserPrincipal.getUserId();
        ChatRoomEnterRequestDto dto = ChatRoomEnterRequestDto.builder()
                .userId(1L)
                .brandId(brandId)
                .build();

        return CommonResponse.success(
                        HttpStatus.OK,
                "로그인 성공",
                chatFacade.enterChatRoom(dto)
        );
    }



}
