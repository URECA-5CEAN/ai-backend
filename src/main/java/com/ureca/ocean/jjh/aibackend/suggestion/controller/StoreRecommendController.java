package com.ureca.ocean.jjh.aibackend.suggestion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ureca.ocean.jjh.aibackend.common.BaseResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.service.StoreRecommendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Slf4j
public class StoreRecommendController {
	
	private final StoreRecommendService storeRecommendService;
	
	@GetMapping("/recommend")
	public ResponseEntity<BaseResponseDto<StoreRecommendResponseDto>> StoreRecommend(StoreRecommendRequestDto request){
		return ResponseEntity.ok(BaseResponseDto.success(storeRecommendService.storeRecommend(request)));
	}
}
