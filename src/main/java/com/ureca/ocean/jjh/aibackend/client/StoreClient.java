package com.ureca.ocean.jjh.aibackend.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ureca.ocean.jjh.aibackend.client.dto.StoreDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreClient {

	private final RestTemplate restTemplate;
	
	public List<StoreDto> getDummyStoreList() {
		// 더미데이터
		List<StoreDto> storeList = new ArrayList<StoreDto>();
		storeList.add(new StoreDto("스타벅스 강남점", "스타벅스", "카페", "서울 강남구 강남대로 396", "37.4979", "127.0276"));
		storeList.add(new StoreDto("CGV 용산아이파크몰", "CGV", "영화관", "서울 용산구 한강대로23길 55", "37.5292", "126.9646"));
		storeList.add(new StoreDto("이마트 성수점", "이마트", "대형마트", "서울 성동구 뚝섬로 377", "37.5445", "127.0563"));
		storeList.add(new StoreDto("맘스터치 신촌점", "맘스터치", "패스트푸드", "서울 서대문구 신촌로 73", "37.5551", "126.9368"));
		storeList.add(new StoreDto("교보문고 광화문점", "교보문고", "서점", "서울 종로구 종로 1", "37.5714", "126.9769"));

		return storeList;
	}
}
