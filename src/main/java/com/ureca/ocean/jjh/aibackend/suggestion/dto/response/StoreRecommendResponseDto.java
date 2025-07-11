package com.ureca.ocean.jjh.aibackend.suggestion.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRecommendResponseDto {

	private UUID storeId;
	private String storeName;
	private String storeCategory;
	private String description;
	private String adrress;
	
	private String reason;
	
}
