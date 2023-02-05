package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.entities.Chat;
import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;
import com.julianduru.messingjarservice.modules.user.UserService;
import com.julianduru.messingjarservice.repositories.ChatMessageRepository;
import com.julianduru.messingjarservice.repositories.ChatRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
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
    public List<ChatPreviewDto> fetchChatPreviews(int page, int size) {
        var auth = AuthUtil.authR();

        String username = "remilekun";

        var previewsMono = userRepository
            .findByUsername(username)
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            .map(user -> {
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
                    var otherUserDetails = new ReactiveBlocker<>(userService.fetchUserDetails(otherUser)).getValue();

                    if (otherUserDetails == null) {
                        continue;
                    }

                    var lastMessage = new ReactiveBlocker<>(chatMessageRepository
                        .findFirstByChatIdOrderByCreatedDateDesc(chat.getId())).getValue();

                    previews.add(
                        ChatPreviewDto.builder()
                            .fullName(otherUserDetails.getFullName())
                            .lastMessage(lastMessage != null ? lastMessage.getMessage() : "")
                            .lastMessageTimeStamp(TimeUtil.formatDateTime(chat.getLastMessageTime()))
                            .profilePictureUrl(otherUserDetails.getProfilePhotoPublicUrl())
                            .build()
                    );
                }

                return previews;
            });

        return new ReactiveBlocker<>(previewsMono).getValue();
    }


}

