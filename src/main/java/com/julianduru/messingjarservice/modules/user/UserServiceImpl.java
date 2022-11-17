package com.julianduru.messingjarservice.modules.user;

import com.julianduru.data.messaging.dto.UserDataUpdate;
import com.julianduru.messingjarservice.dto.UserDto;
import com.julianduru.messingjarservice.entities.Settings;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.user.dto.UserUpdateDto;
import com.julianduru.messingjarservice.repositories.SettingsRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.oauthservicelib.modules.user.UserDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    private final SettingsRepository settingsRepository;



    @Override
    public Mono<User> saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }


    @Override
    public Mono<Void> updateUser(String username, UserUpdateDto userUpdateDto) {
        return settingsRepository
            .findByUsername(username)
            .switchIfEmpty(Mono.just(new Settings()))
            .flatMap(
                s -> {
                    s.setUsername(username);
                    s.setEnableEmails(userUpdateDto.isEnableEmails());
                    return settingsRepository.save(s);
                }
            )
            .map(
                settings -> userDataService.processOAuthUserDataUpdate(
                    new UserDataUpdate(
                        username,
                        userUpdateDto.getFirstName(),
                        userUpdateDto.getLastName(),
                        userUpdateDto.getEmail(),
                        userUpdateDto.getProfilePhotoRef()
                    )
                )
            )
            .then();
    }


}


