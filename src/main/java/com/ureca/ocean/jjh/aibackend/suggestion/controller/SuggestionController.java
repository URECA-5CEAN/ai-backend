package com.ureca.ocean.jjh.aibackend.suggestion.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ocean.jjh.aibackend.common.BaseResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.TitleRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.service.RecommendService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Slf4j
public class SuggestionController {
	
	private final RecommendService recommendService;
	
	@GetMapping("/recommend/store")
	public ResponseEntity<BaseResponseDto<StoreRecommendResponseDto>> storeRecommend(
			@Parameter(hidden = true) @RequestHeader("X-User-email") String encodedEmail,
			@RequestBody StoreRecommendRequestDto request){
		String email = URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);
		return ResponseEntity.ok(BaseResponseDto.success(recommendService.storeRecommend(email, request)));
	}
	
	@GetMapping("/recommend/title")
	public ResponseEntity<BaseResponseDto<List<TitleRecommendResponseDto>>> titleRecommend(
			@Parameter(hidden = true) @RequestHeader("X-User-email") String encodedEmail){
		String email = URLDecoder.decode(encodedEmail, StandardCharsets.UTF_8);
		List<TitleRecommendResponseDto> result = recommendService.titleRecommend(email);
		return ResponseEntity.ok(BaseResponseDto.success(result));
	}
}
