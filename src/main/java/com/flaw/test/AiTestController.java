package com.flaw.test;

import com.flaw.utils.ApiResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AiTestController {
    private final ChatClient chatClient;

    public AiTestController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ai")
    public ResponseEntity<ApiResponse<?>> testAi(){
        return ResponseEntity.ok(new ApiResponse<>(true, "success", chatClient.prompt()
                .user("Say hi to Limuel Camangon")
                .call()
                .content()));
    }
}
