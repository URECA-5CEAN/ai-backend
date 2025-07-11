package com.ureca.ocean.jjh.aibackend.suggestion.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.ureca.ocean.jjh.aibackend.common.entity.BaseEntity;
import com.ureca.ocean.jjh.aibackend.suggestion.entity.enums.Membership;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "membership", nullable = false)
    private Membership membership;

    @Column(name = "title")
    private String title;

}
