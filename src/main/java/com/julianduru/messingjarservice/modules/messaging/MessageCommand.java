package com.julianduru.messingjarservice.modules.messaging;

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


    public enum Type {

        INITIALIZE_CHAT

    }


}

