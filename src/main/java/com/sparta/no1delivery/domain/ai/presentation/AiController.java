package com.sparta.no1delivery.domain.ai.presentation;

import com.sparta.no1delivery.domain.ai.application.AiService;
import com.sparta.no1delivery.domain.ai.presentation.dto.AiRequest;
import com.sparta.no1delivery.domain.ai.presentation.dto.MenuNamingResponse;
import com.sparta.no1delivery.domain.ai.presentation.dto.StoreDescriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/menu-naming")
    public ResponseEntity<List<MenuNamingResponse>> generateMenuNamings(@RequestBody AiRequest request) {
        return ResponseEntity.ok(aiService.generateMenuNamings(request.prompt()));
    }

    @PostMapping("/store-description")
    public ResponseEntity<List<StoreDescriptionResponse>> generateStoreDescriptions(@RequestBody AiRequest request) {
        return ResponseEntity.ok(aiService.generateStoreDescriptions(request.prompt()));
    }
}
