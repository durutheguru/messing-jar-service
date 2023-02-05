package com.julianduru.messingjarservice.modules.user;

import com.julianduru.data.messaging.dto.UserDataUpdate;
import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.dto.UserDto;
import com.julianduru.messingjarservice.entities.Settings;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.user.components.UserDetailsReader;
import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import com.julianduru.messingjarservice.repositories.SettingsRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.oauthservicelib.modules.user.UserDataService;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * created by julian on 27/08/2022
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    private final UserDataService userDataService;


    private final UserDetailsReader userDetailsReader;


    private final SettingsRepository settingsRepository;


    private final NotificationService notificationService;


    private final FileUploadRepository fileUploadRepository;



    @Override
    public Mono<User> saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }


    @Override
    public Mono<Void> updateUser(String username, UserDataDto userDataDto) {
        return settingsRepository
            .findByUsername(username)
            .switchIfEmpty(Mono.just(new Settings()))
            .flatMap(
                s -> {
                    s.setUsername(username);
                    s.setEnableEmails(userDataDto.isEnableEmails());
                    return settingsRepository.save(s);
                }
            )
            .map(
                settings -> userDataService.processOAuthUserDataUpdate(
                    new UserDataUpdate(
                        username,
                        userDataDto.getFirstName(),
                        userDataDto.getLastName(),
                        userDataDto.getEmail(),
                        userDataDto.getProfilePhotoRef()
                    )
                )
            )
            .map(
                status -> {
                    if (StringUtils.hasText(userDataDto.getProfilePhotoRef())) {
                        var upload = fileUploadRepository.findByReference(userDataDto.getProfilePhotoRef());
                        upload.ifPresent(fileUpload -> userDataDto.setProfilePhotoPublicUrl(fileUpload.getPublicUrl()));
                    }

                    if (status.is(OperationStatus.Value.SUCCESS)) {
                        return notificationService.writeUserNotification(
                            username, ServiceConstants.NotificationType.PROFILE_DETAILS_UPDATE, userDataDto
                        );
                    }

                    return status;
                }
            )
            .then();
    }


    @Override
    public Mono<UserDataDto> fetchUserDetails(String username) {
        return userDetailsReader.fetchUserDetails(username);
    }


    @Override
    public Mono<UserDataDto> fetchUserDetails(ObjectId userId) {
        return userRepository.findById(userId)
            .flatMap(user -> userDetailsReader.fetchUserDetails(user.getUsername()));
    }


}


