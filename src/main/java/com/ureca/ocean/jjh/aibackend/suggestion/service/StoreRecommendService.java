package com.ureca.ocean.jjh.aibackend.suggestion.service;

import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;

public interface StoreRecommendService {
	StoreRecommendResponseDto storeRecommend(StoreRecommendRequestDto request);
}
