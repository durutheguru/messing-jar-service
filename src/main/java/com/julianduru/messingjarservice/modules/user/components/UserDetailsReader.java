package com.julianduru.messingjarservice.modules.user.components;

import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.modules.user.dto.UserDto;
import com.julianduru.messingjarservice.entities.Settings;
import com.julianduru.messingjarservice.modules.user.dto.OAuthUserData;
import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import com.julianduru.messingjarservice.modules.user.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * created by julian on 25/11/2022
 */
@Component
@RequiredArgsConstructor
public class UserDetailsReader {

    private final UserSaver userSaver;

    private final WebClient oauthServerWebClient;

    private final SettingsRepository settingsRepository;

    private final FileUploadRepository fileUploadRepository;


    public Mono<UserDataDto> fetchUserDetails(String username) {
        var user = userSaver.saveUser(
            UserDto
                .builder()
                .username(username)
                .build()
        ).toFuture().join();

        var userSettingsMono = settingsRepository
            .findByUsername(username)
            .switchIfEmpty(Mono.just(new Settings()));

        var oauthUserDataMono = oauthServerWebClient.get()
            .uri(builder -> builder
                .path("/api/v1/user")
                .queryParam("username", username)
                .build()
            )
            .retrieve()
            .bodyToMono(OAuthUserData.class);

        var userDetailsMono = Mono.zip(userSettingsMono, oauthUserDataMono);

        return userDetailsMono
            .map(data -> {
                var settings = data.getT1();
                var oauthUserData = data.getT2();

                var dto = new UserDataDto();

                dto.setUserId(user.getIdString());
                dto.setUsername(oauthUserData.getUsername());
                dto.setEmail(oauthUserData.getEmail());
                dto.setFirstName(oauthUserData.getFirstName());
                dto.setLastName(oauthUserData.getLastName());
                dto.setEnableEmails(settings.isEnableEmails());
                if (oauthUserData.getAdditionalInfo() != null) {
                    dto.setProfilePhotoRef(oauthUserData.getAdditionalInfo().get("profile_photo"));
                }

                if (StringUtils.hasText(dto.getProfilePhotoRef())) {
                    var profileUpload = fileUploadRepository.findByReference(dto.getProfilePhotoRef());
                    profileUpload.ifPresent(fileUpload -> dto.setProfilePhotoPublicUrl(fileUpload.getPublicUrl()));
                }

                return dto;
            })
            .onErrorResume(Mono::error);
    }


}

