package com.julianduru.messingjarservice.modules.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 21/01/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCommand {


    private String username;


    private Type type;


    private String payload;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Type {

        INITIALIZE_CHAT,

        CHAT_MESSAGE,

        INITIALIZE_GROUP,

        GROUP_MESSAGE,

    }


}

