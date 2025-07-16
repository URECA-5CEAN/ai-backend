package com.ureca.ocean.jjh.aibackend.suggestion.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.TitleRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.service.RecommendService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

	private final ChatClient chatClient;

	@Override
	public StoreRecommendResponseDto storeRecommend(StoreRecommendRequestDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TitleRecommendResponseDto> titleRecommend(Long userId) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("최근 이용 제휴처", "스타벅스");
		userInfo.put("최근 이용 혜택", "포인트 적립");
		userInfo.put("즐겨찾기", "파리바게트");
		
		String prompt =String.format("""
				사용자의 데이터를 기반으로 칭호를 3가지 제시하라. 각 칭호는 제목(title)과 그 이유(reason)를 가진다.
				
				예를 들면
				
				title: 커피 매니아
				reason: 최근 이용내역에 카페 방문 5회 이상
				
				사용자 정보:
				최근 이용 제휴처: %s
				최근 이용 혜택: %s
				즐겨찾기 목록: %s
				
				결과는 반드시 아래 예시를 참고하여 같은 형식의 **JSON 배열**로 반환하라:
				[
					{ "title":"...", "reason":"..." },
					{ "title":"...", "reason":"..." },
					{ "title":"...", "reason":"..." }
				]
				
				""",
					userInfo.get("최근 이용 제휴처"),
					userInfo.get("최근 이용 혜택"),
					userInfo.get("즐겨찾기")
				);
		
		String guideLine = """
				사용자 데이터를 분석하여 칭호를 추천해줘야 한다.
				""";
		
		String response = chatClient.prompt()
								.system(guideLine)
								.user(prompt)
								.call()
								.content();
		
		return parseResponse(response);
	}
	
	
	
	
	private List<TitleRecommendResponseDto> parseResponse(String response) {
        List<TitleRecommendResponseDto> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            TitleRecommendResponseDto[] array = mapper.readValue(response, TitleRecommendResponseDto[].class);
            result = Arrays.asList(array);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
	
	
}
