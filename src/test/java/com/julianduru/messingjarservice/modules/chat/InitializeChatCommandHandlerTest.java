package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.BaseIntegrationTest;
import com.julianduru.messingjarservice.data.UserDataProvider;
import com.julianduru.messingjarservice.modules.messaging.MessageCommand;
import com.julianduru.messingjarservice.util.ReactiveBlocker;
import com.julianduru.messingjarservice.util.ReactiveListBlocker;
import com.julianduru.util.api.OperationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 23/01/2023
 */
public class InitializeChatCommandHandlerTest extends BaseIntegrationTest {

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private InitializeChatCommandHandler chatCommandHandler;



//    @Test
//    public void initializationOfAChatBetweenTwoUsers() throws Exception {
//        var users = new ReactiveListBlocker<>(
//            userDataProvider.save(2)
//        ).getValue();
//
//        var user1 = users.get(0);
//        var user2 = users.get(1);
//
//        var command = new MessageCommand();
//        command.setUsername(user1.getUsername());
//        command.setType(MessageCommand.Type.INITIALIZE_CHAT);
//        command.setPayload(
//            String.format(
//            """
//            {
//            "username": "%s"
//            }
//            """, user2.getUsername()
//            )
//        );
//
//        var response = new ReactiveBlocker<>(chatCommandHandler.handle(command)).getValue();
//        assertThat(response.getStatus()).isEqualTo(OperationStatus.Value.SUCCESS);
//
//    }


    @Test
    public void initializationOfAChatBetweenTwoUsers() throws Exception {
        var command = new MessageCommand();
        command.setUsername("0652065201");
        command.setType(MessageCommand.Type.INITIALIZE_CHAT);
        command.setPayload(
            String.format(
                """
                {
                "username": "%s"
                }
                """, "099971323X"
            )
        );

        var response = new ReactiveBlocker<>(chatCommandHandler.handle(command)).getValue();
        assertThat(response.getStatus()).isEqualTo(OperationStatus.Value.SUCCESS);

    }

}
