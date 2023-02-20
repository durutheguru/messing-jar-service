package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.entities.Chat;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;
import com.julianduru.messingjarservice.modules.user.UserService;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import com.julianduru.messingjarservice.util.AuthUtil;
import com.julianduru.messingjarservice.util.ReactiveBlocker;
import com.julianduru.messingjarservice.util.ReactiveListBlocker;
import com.julianduru.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * created by julian on 05/02/2023
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final ChatRepository chatRepository;


    private final ChatMessageRepository chatMessageRepository;


    private final UserRepository userRepository;


    private final UserService userService;


    @Override
    public List<ChatPreviewDto> fetchChatPreviews(String username, int page, int size) throws ExecutionException, InterruptedException {
        return userRepository.findByUsername(username)
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            .map(
                user -> {
                    var chats = chatRepository.findByUser1OrUser2(
                        user.getId(),
                        user.getId(),
                        PageRequest.of(
                            page, size,
                            Sort.by(Sort.Direction.DESC, "lastMessageTime")
                        )
                    );

                    var previews = new ArrayList<ChatPreviewDto>();
                    var chatList= new ReactiveListBlocker<>(chats).getValue();
                    for (Chat chat : chatList) {
                        var otherUser = chat.getUser1().equals(user.getId()) ? chat.getUser2() : chat.getUser1();
                        var otherUserDetails = userService.fetchUserDetails(otherUser).toFuture().join();

                        if (otherUserDetails == null) {
                            continue;
                        }

                        var lastMessage = new ReactiveBlocker<>(chatMessageRepository
                            .findFirstByChatIdOrderByCreatedDateDesc(chat.getId())).getValue();

                        previews.add(
                            ChatPreviewDto.builder()
                                .chatId(chat.getId() != null ? chat.getId().toString(): "")
                                .username(otherUserDetails.getUsername())
                                .fullName(otherUserDetails.getFullName())
                                .lastMessage(lastMessage != null ? lastMessage.getMessage() : "")
                                .lastMessageTimeStamp(TimeUtil.formatDateTime(chat.getLastMessageTime()))
                                .profilePictureUrl(otherUserDetails.getProfilePhotoPublicUrl())
                                .build()
                        );
                    }

                    return previews;
                }
            )
            .toFuture().get();
    }


}

