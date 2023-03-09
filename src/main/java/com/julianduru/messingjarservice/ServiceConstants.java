package com.julianduru.messingjarservice;

/**
 * created by julian on 02/09/2022
 */
public interface ServiceConstants {


    String API_BASE = "/api/v1";


    interface NotificationType {

        String PROFILE_DETAILS_UPDATE = "profile-details-update";

        String CHAT_HISTORY = "chat-history";

        String GROUP_HISTORY = "group-history";

        String NEW_CHAT_MESSAGE = "new-chat-message";

        String NEW_GROUP_MESSAGE = "new-group-message";

    }


}


