package com.ureca.ocean.jjh.aibackend.suggestion.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.ocean.jjh.aibackend.client.StoreClient;
import com.ureca.ocean.jjh.aibackend.client.UserClient;
import com.ureca.ocean.jjh.aibackend.client.dto.StoreDto;
import com.ureca.ocean.jjh.aibackend.client.dto.UserDto;
import com.ureca.ocean.jjh.aibackend.common.exception.AiException;
import com.ureca.ocean.jjh.aibackend.common.exception.ErrorCode;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.UserInfoDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.TitleRecommendResponseDto;
import com.ureca.ocean.jjh.aibackend.suggestion.service.RecommendService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

	private final ChatClient chatClient;
	private final UserClient userClient;
	private final StoreClient storeClient;

	@Override
	public StoreRecommendResponseDto storeRecommend(@RequestBody StoreRecommendRequestDto request) {

		List<StoreDto> storeList = storeClient.getDummyStoreList();
		
		String storeListToString = storeList.stream()
											.map((store) -> String.format("- %s (%s, %s, %s, %s, %s)", 
													store.getName(), store.getBrandName(), store.getCategory(), store.getAddress(), store.getLatitude(), store.getLongitude()))
											.collect(Collectors.joining("\n"));
													
		String prompt = String.format("""
				당신은 사용자 정보를 기반으로 매장을 추천하는 추천 시스템입니다.

				사용자의 위치 정보
				- 위도: %s
				- 경도: %s
				
				사용자의 이용 정보
				- 최근 이용 제휴처 : 롯데시네마, 메가박스
				- 즐겨찾기 목록  : CGV

				아래는 추천할 수 있는 매장 목록이다. 괄호 안에는 순서대로 브랜드, 카테고리, 주소, 위도, 경도 정보를 나타낸다.
				%s

				위 매장 중 사용자 위치와 가까우면서도 인기 있을 만한 매장 하나를 추천하라.
				반드시 아래 형식의 **JSON 객체**로 응답해야 합니다:

				{
				  "store": {
				    "name": "[매장명]",
				    "brandName": "[브랜드명]",
				    "category": "[카테고리]",
				    "address": "[주소]",
				    "latitude": "[위도]",
				    "longitude": "[경도]"
				  },
				  "reason": "[추천 이유]"
				}
				""",
				    request.getLatitude(),
				    request.getLongitude(),
				    storeListToString
				);
		
		String guideLine = """
				사용자 데이터를 분석하여 제휴처를 추천한다.
				""";
		
		String response = chatClient.prompt()
								.system(guideLine)
								.user(prompt)
								.call()
								.content();
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			StoreRecommendResponseDto storeRecommend = objectMapper.readValue(response, StoreRecommendResponseDto.class);
			return storeRecommend;
		}catch (IOException e) {
			throw new AiException(ErrorCode.PARSING_EEROR);
		}
	}

	@Override
	public List<TitleRecommendResponseDto> titleRecommend(String email) {
		
		UserDto user = userClient.getDummyUser();
		
		// 수집할 정보들 (현재는 더미 데이터)
		UserInfoDto userInfo = new UserInfoDto();
		userInfo.setGender("male");
		userInfo.setNickname("나비");
		userInfo.setRecentVisitedStores(List.of("스타벅스", "스타벅스", "스타벅스", "파리바게트", "CGV"));
		userInfo.setRecentBenefits(List.of("할인", "할인", "할인", "할인", "증정품"));
		userInfo.setFavorites(List.of("스타벅스"));
		
		String prompt = String.format("""
				사용자의 데이터를 기반으로 칭호를 3가지 제시하라. 아래 예시처럼 각 칭호는 제목(title)과 그 이유(reason)를 가진다.
				
				예시
				title: 커피 매니아
				reason: 최근 이용내역에 카페 방문 5회 이상
				
				사용자 정보
				성별: %s
				닉네임: %s
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
					userInfo.getGender(),
					userInfo.getNickname(),
					String.join(", ", userInfo.getRecentVisitedStores()),
				    String.join(", ", userInfo.getRecentBenefits()),
				    String.join(", ", userInfo.getFavorites())
				);
		
		String guideLine = """
				사용자 데이터를 분석하여 칭호를 그에 맞는 개성있는 생성 한다.
				""";
		
		String response = chatClient.prompt()
								.system(guideLine)
								.user(prompt)
								.call()
								.content();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
            TitleRecommendResponseDto[] array = objectMapper.readValue(response, TitleRecommendResponseDto[].class);
            return Arrays.asList(array);
		} catch(IOException e){
			throw new AiException(ErrorCode.PARSING_EEROR);
		}
	}
	
}
