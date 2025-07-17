package com.ureca.ocean.jjh.aibackend.suggestion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRecommendRequestDto {
	private String latitude;
	private String longitude;
	private int redius;
}
