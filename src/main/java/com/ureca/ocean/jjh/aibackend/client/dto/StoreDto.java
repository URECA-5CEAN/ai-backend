package com.ureca.ocean.jjh.aibackend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {

	private String name;
	private String brandName;
	private String category;
	private String address;
	private String latitude;
	private String longitude;
}
