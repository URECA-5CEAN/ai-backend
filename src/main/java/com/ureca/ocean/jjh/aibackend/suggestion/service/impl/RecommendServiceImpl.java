package com.ureca.ocean.jjh.aibackend.suggestion.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.ocean.jjh.aibackend.client.StoreClient;
import com.ureca.ocean.jjh.aibackend.client.UserClient;
import com.ureca.ocean.jjh.aibackend.client.dto.StoreDto;
import com.ureca.ocean.jjh.aibackend.client.dto.StoreUsageDto;
import com.ureca.ocean.jjh.aibackend.client.dto.UserDto;
import com.ureca.ocean.jjh.aibackend.common.exception.AiException;
import com.ureca.ocean.jjh.aibackend.common.exception.ErrorCode;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.request.StoreRecommendRequestDto;
import com.ureca.ocean.jjh.aibackend.suggestion.dto.response.StoreRecommendDto;
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
	public StoreRecommendResponseDto storeRecommend(String email, StoreRecommendRequestDto request) {

		List<StoreDto> storeList = storeClient.getStores(request.getKeyword(), request.getCategory(),
														 request.getLatMin(), request.getLatMax(), 
														 request.getLngMin(), request.getLngMax(), 
														 request.getCenterLat(), request.getCenterLng());
		
		String storeListString = storeList.stream()
			    .map(store -> String.format("""
			        {
			          "id": %s,
			          "name": "%s",
			          "category": "%s",
			          "latitude": %.6f,
			          "longitude": %.6f,
			        }
			        """,
			        store.getId(),
			        store.getName(),
					store.getCategory(),
					store.getLatitude(),
					store.getLongitude())
				)
			    .collect(Collectors.joining(",\n", "[\n", "\n]"));
		
		UserDto user = userClient.getUserByEmail(email);
		
		String userInfo = getUserInfo(email);
		String prompt = String.format("""
				당신은 개인화된 매장 추천을 수행하는 AI 시스템입니다.

				[사용자 정보]
				%s
				
				[추천 대상 매장 목록]
				%s
				
				당신의 역할은 사용자 정보와 매장 목록을 바탕으로 **사용자에게 가장 적합한 매장 하나를 엄선하여 추천**하는 것입니다.
				추천 시에는 사용자의 위치, 즐겨찾기, 방문 이력, 관심 카테고리 등을 고려하세요.
				추천 이유는 **사용자와 매장의 연결 고리를 구체적으로** 설명해야 하며, 단순한 나열이나 일반적인 설명은 피해야 합니다.
				
				📌 응답은 반드시 **아래 JSON 형식만**으로 출력해야 하며, 그 외의 문장은 포함하지 마세요:
				
				```json
				{
				  "storeId": "UUID 형태의 storeId",
				  "reason": "이 매장을 추천하는 구체적이고 설득력 있는 이유"
				}
				""",
				storeListString,
				userInfo
				);
		
		String guideLine = """
				당신은 사용자 맞춤형 제휴처를 정확히 선별하고 추천하는 AI입니다.

				당신의 임무는 사용자의 취향, 이용 기록, 현재 위치 등을 종합적으로 분석하여 수많은 매장 중에서 가장 이상적인 매장 하나를 찾아주는 것입니다.
				사용자에게 실질적인 가치를 제공하는 추천이 되도록 하며, 반드시 지정된 JSON 포맷만 출력해야 합니다.
				""";
		
		String response = chatClient.prompt()
								.system(guideLine)
								.user(prompt)
								.call()
								.content();
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			StoreRecommendDto storeRecommend = objectMapper.readValue(response, StoreRecommendDto.class);
			
			StoreDto store = storeClient.getStoreDetail(storeRecommend.getStoreId());
			String reason = storeRecommend.getReason();
			return new StoreRecommendResponseDto(store, reason);
		}catch (IOException e) {
			throw new AiException(ErrorCode.PARSING_ERROR);
		}
	}

	@Override
	public List<TitleRecommendResponseDto> titleRecommend(String email) {
		
		UserDto user = userClient.getUserByEmail(email);
		String userInfo = getUserInfo(email);
		
		String prompt = String.format("""
				사용자의 데이터를 분석하여, 해당 사용자에게 어울리는 **칭호(title)** 3개를 지어라.
				각 칭호는 단순한 설명이 아닌 **개성 있고 창의적인 형태**여야 하며, LLM의 상상력을 발휘할 것.

				🎯 다음 기준을 고려하라:
				- **재미** 또는 **감성**을 담는다 (예: "적립의 요정", "카페 마스터", "무비 헌터")
				- **브랜드 선호**, **이용 패턴**, **방문 빈도** 등을 토대로 칭호를 만든다
				- 칭호에 숫자, 이모지, 밈 표현 등도 허용한다 (단, 과하지 않게)

				📋 형식:
				각 칭호는 JSON 배열 형식으로 반환한다. 예시는 아래와 같다.

				[
				  { "title": "쿠폰 장인", "reason": "혜택 사용 빈도가 높은 사용자입니다." },
				  { "title": "투썸의 사나이", "reason": "최근 투썸플레이스를 5회 이상 방문했습니다." },
				  { "title": "적립의 요정 🧚", "reason": "즐겨찾기 브랜드를 꾸준히 방문하며 포인트를 모읍니다." }
				]

				👤 사용자 정보:
				%s
				""", userInfo);

		
		String guideline = """
				- 칭호는 사용자의 개성을 드러내야 하며, 너무 일반적이지 않도록 한다.
				- 예를 들어 단순히 '카페 애호가'보다 '라떼 탐험가', '에스프레소의 길잡이' 같은 표현을 우선시한다.
				- 필요하다면 2030 세대가 공감할 만한 표현(밈/문화 키워드)도 소량 포함 가능.
				- 단, 유머가 정보 전달을 해치지 않도록 균형을 유지할 것.
				""";

		
		String response = chatClient.prompt()
								.system(guideline)
								.user(prompt)
								.call()
								.content();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
            TitleRecommendResponseDto[] array = objectMapper.readValue(response, TitleRecommendResponseDto[].class);
            return Arrays.asList(array);
		} catch(IOException e){
			throw new AiException(ErrorCode.PARSING_ERROR);
		}
	}
	
	
	private String getUserInfo(String email) {
	    List<StoreDto> bookmarks = storeClient.getAllBookmarks(email);
	    List<StoreUsageDto> usages = storeClient.getAllUsages(email);

	    // 즐겨찾기 브랜드 (중복 제거 + 순서 유지)
	    Set<String> favoriteBrands = bookmarks.stream()
	            .map(StoreDto::getBrandName)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toCollection(LinkedHashSet::new));

	    // storeId → StoreDto 매핑
	    Map<UUID, StoreDto> storeDtoMap = bookmarks.stream()
	            .collect(Collectors.toMap(StoreDto::getId, Function.identity()));

	    // 최근 이용 기록을 브랜드별로 그룹화
	    Map<String, List<StoreUsageDto>> usageByBrand = usages.stream()
	            .sorted(Comparator.comparing(StoreUsageDto::getVisitedAt).reversed()) // 최근 방문순
	            .filter(usage -> storeDtoMap.containsKey(usage.getStoreId())) // 대응되는 매장만
	            .collect(Collectors.groupingBy(usage -> {
	                StoreDto store = storeDtoMap.get(usage.getStoreId());
	                return store.getBrandName();
	            }, LinkedHashMap::new, Collectors.toList()));

	    StringBuilder recentUsageBuilder = new StringBuilder();

	    if (usageByBrand.isEmpty()) {
	        recentUsageBuilder.append("없음");
	    } else {
	        for (Map.Entry<String, List<StoreUsageDto>> entry : usageByBrand.entrySet()) {
	            String brand = entry.getKey();
	            List<StoreUsageDto> usageList = entry.getValue();
	            int count = usageList.size();
	            String lastVisited = usageList.get(0).getVisitedAt().toString(); // 가장 최근 방문

	            recentUsageBuilder.append(String.format("- %s: %d회 방문 (최근: %s)\n", brand, count, lastVisited));
	        }
	    }

	    String favoritesFormatted = favoriteBrands.isEmpty()
	            ? "없음"
	            : String.join(", ", favoriteBrands);

	    return String.format("""
	            👤 사용자 이용 정보 요약

	            🔖 즐겨찾는 브랜드:
	            %s

	            🕘 최근 이용 브랜드:
	            %s
	            """, favoritesFormatted, recentUsageBuilder.toString().trim());
	}

	
}
