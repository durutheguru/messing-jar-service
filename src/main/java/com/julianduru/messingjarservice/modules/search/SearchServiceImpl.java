package com.julianduru.messingjarservice.modules.search;

import com.julianduru.messingjarservice.modules.search.component.UserSearcher;
import com.julianduru.messingjarservice.modules.search.dto.SearchResult;
import com.julianduru.messingjarservice.modules.search.dto.UserSearchResult;
import com.julianduru.messingjarservice.util.ReactiveListBlocker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * created by julian on 26/12/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {


    private final UserSearcher userSearcher;


    @Override
    public Mono<SearchResult> search(String query) {
        var userSearchResults = userSearcher.searchUsers(query);
        return Mono.just(
            SearchResult.builder()
                .userSearchResults(userSearchResults.collectList().toFuture().join())
                .build()
        );
    }


}
