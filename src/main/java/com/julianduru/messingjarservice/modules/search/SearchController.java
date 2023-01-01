package com.julianduru.messingjarservice.modules.search;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.modules.search.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * created by julian on 26/12/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(SearchController.PATH)
public class SearchController {


    public static final String PATH = ServiceConstants.API_BASE + "/search";


    private final SearchService searchService;


    @GetMapping
    public Mono<SearchResult> search(@RequestParam("query") String query) {
        return searchService.search(query);
    }


}


