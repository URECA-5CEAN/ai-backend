package com.ureca.ocean.jjh.aibackend.common;

import com.ureca.ocean.jjh.aibackend.common.exception.ErrorCode;
import com.ureca.ocean.jjh.aibackend.common.exception.ErrorResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto<T> {

    private Integer statusCode;
    private String message;
    private T data;

    public static <T> BaseResponseDto<T> success(T data) {
        return BaseResponseDto.<T>builder()
                .statusCode(200)
                .message("success")
                .data(data)
                .build();
    }

    public static BaseResponseDto<ErrorResponseDto> fail(ErrorCode errorCode) {
        //service layer -> global exception handler -> this method ( controller를 거치지 않음 )
        //Errorcode에 있는 정보로 ErrorResponseDto를 만든다.
        return BaseResponseDto.<ErrorResponseDto>builder()
                .statusCode(errorCode.getCode())
                .message("fail")
                .data(ErrorResponseDto.of(errorCode))
                .build();
    }
}
