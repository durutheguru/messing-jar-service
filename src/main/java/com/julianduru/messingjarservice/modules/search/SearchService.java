package com.julianduru.messingjarservice.modules.search;

import com.julianduru.messingjarservice.modules.search.dto.SearchResult;
import reactor.core.publisher.Mono;

/**
 * created by julian on 26/12/2022
 */
public interface SearchService {


    Mono<SearchResult> search(String query);


}
