package com.julianduru.messingjarservice.modules.search.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.julianduru.messingjarservice.modules.user.dto.OAuthUserData;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * created by julian on 26/12/2022
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class OAuthUserSearchResultsPageType extends PageImpl<OAuthUserData> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public OAuthUserSearchResultsPageType(
        @JsonProperty("content") List<OAuthUserData> content,
        @JsonProperty("number") int number,
        @JsonProperty("size") int size,
        @JsonProperty("totalElements") Long totalElements
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }

}
