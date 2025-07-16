package com.ureca.ocean.jjh.aibackend.suggestion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRecommendRequestDto {
	private String request;
}
