package com.julianduru.messingjarservice.modules.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by julian on 26/12/2022
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {


    private List<UserSearchResult> userSearchResults;


}
