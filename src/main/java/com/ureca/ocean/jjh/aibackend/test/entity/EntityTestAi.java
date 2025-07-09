package com.ureca.ocean.jjh.aibackend.test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "entity_test_1")
@Data
public class EntityTestAi {
	@Id
	private Long id;
	private String text;
	
}
