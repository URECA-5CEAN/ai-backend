package com.ureca.ocean.jjh.aibackend.suggestion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ocean.jjh.aibackend.common.BaseResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.TitleRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.service.RecommendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Slf4j
public class SuggestionController {
	
	private final RecommendService recommendService;
	
	@GetMapping("/recommend/store")
	public ResponseEntity<BaseResponseDto<StoreRecommendResponseDto>> storeRecommend(StoreRecommendRequestDto request){
		return ResponseEntity.ok(BaseResponseDto.success(recommendService.storeRecommend(request)));
	}
	
	@GetMapping("/recommend/title")
	public ResponseEntity<BaseResponseDto<List<TitleRecommendResponseDto>>> titleRecommend(@RequestParam(name="userId") Long userId){
		return ResponseEntity.ok(BaseResponseDto.success(recommendService.titleRecommend(userId)));
	}
}
