//package com.ureca.ocean.jjh.aibackend.suggestion.entity;
//
//import java.util.UUID;
//
//import com.ureca.ocean.jjh.aibackend.common.entity.BaseEntity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "stores")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Store extends BaseEntity{
//
//	@Id
//	@GeneratedValue(generator = "uuid2")
//	@Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
//	private UUID id;
//	
//	@Column(name = "name", nullable = false)
//	private String name;
//	
//	@Column(name = "category", nullable = false)
//	private String category;
//	
//	@Column(name = "description")
//	private String description;
//	
//	@Column(name = "address")
//	private String address;
//	
//	@Column(name = "latitude")
//	private String latitude;
//	
//	@Column(name = "longitude")
//	private String longitude;
//}
