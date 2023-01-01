package com.julianduru.messingjarservice.modules.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * created by julian on 26/12/2022
 */
public class ParameterizedPageType<T> extends PageImpl<T> {


    public ParameterizedPageType(
        @JsonProperty("content") List<T> content,
        @JsonProperty("number") int number,
        @JsonProperty("size") int size,
        @JsonProperty("totalElements") Long totalElements
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }


}
