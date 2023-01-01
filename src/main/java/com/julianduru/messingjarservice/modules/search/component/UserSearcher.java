package com.julianduru.messingjarservice.modules.search.component;

import com.julianduru.fileuploader.api.FileUpload;
import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.modules.search.dto.OAuthUserSearchResultsPageType;
import com.julianduru.messingjarservice.modules.search.dto.ParameterizedPageType;
import com.julianduru.messingjarservice.modules.search.dto.UserSearchResult;
import com.julianduru.messingjarservice.modules.user.dto.OAuthUserData;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.messingjarservice.util.ReactiveBlocker;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * created by julian on 26/12/2022
 */
@Component
@RequiredArgsConstructor
public class UserSearcher {


    private final UserRepository userRepository;


    private final WebClient oauthServerWebClient;


    private final FileUploadRepository fileUploadRepository;



    public Flux<UserSearchResult> searchUsers(String query) {
        var reactiveBlocker = new ReactiveBlocker<Flux<UserSearchResult>>(
            oauthServerWebClient.get()
                .uri("/api/v1/user/search?query=" + query)
                .retrieve()
                .bodyToMono(OAuthUserSearchResultsPageType.class)
                .map(response -> {
                    if (response == null || response.isEmpty()) {
                        return Flux.empty();
                    }

                    var pageContent = response.getContent();
                    var usernames = new ArrayList<String>();
                    var fileRefs = new ArrayList<String>();

                    pageContent.stream().forEach(
                        o -> {
                            usernames.add(o.getUsername());
                            if (o.getAdditionalInfo() != null) {
                                var profilePhotoRef = o.getAdditionalInfo().get("profile_photo");
                                if (StringUtils.hasText(profilePhotoRef)) {
                                    fileRefs.add(profilePhotoRef);
                                }
                            }
                        }
                    );

                    var users = userRepository.findByUsernameIn(usernames);
                    var uploads = fileUploadRepository.findByReferenceIn(fileRefs);

                    return users.map(
                            u -> pageContent.stream()
                                .filter(p -> p.getUsername().equalsIgnoreCase(u.getUsername()))
                                .findFirst()
                                .map(
                                    p -> {
                                        String profilePhotoRef = "";
                                        FileUpload profileUpload = null;
                                        var additionalInfo = p.getAdditionalInfo();

                                        if (additionalInfo != null) {
                                            profilePhotoRef = additionalInfo.get("profile_photo");
                                            profileUpload = uploads.stream().filter(
                                                upload -> upload.getReference()
                                                    .equalsIgnoreCase(additionalInfo.get("profile_photo"))
                                            ).findFirst().orElse(null);
                                        }

                                        return UserSearchResult.builder()
                                            .firstName(p.getFirstName())
                                            .lastName(p.getLastName())
                                            .email(p.getEmail())
                                            .username(p.getUsername())
                                            .profilePhotoRef(profilePhotoRef)
                                            .profilePhotoUrl(
                                                profileUpload != null ? profileUpload.getPublicUrl() : null
                                            )
                                            .build();
                                    }
                                )
                                .orElse(null)
                        ).filter(Objects::nonNull);
                })
        );

        return reactiveBlocker.getValue();
    }


}
