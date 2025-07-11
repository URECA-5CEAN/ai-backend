package com.ureca.ocean.jjh.aibackend.suggestion.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.repository.StoreRepository;
import com.ureca.ocean.jjh.aibackend.suggestion.service.StoreRecommendService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreReommendServiceImpl implements StoreRecommendService{

	private final ChatClient chatClient;
	private final StoreRepository storeRepository;

	@Override
	public StoreRecommendResponseDto storeRecommend(StoreRecommendRequestDto request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
