package com.julianduru.messingjarservice.modules.group.dto;

import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by Julian Duru on 09/03/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInitializationResponseDto {


    private UserDataDto initiatorDetails;

    private List<UserDataDto> otherMembersDetails;

    private List<GroupMessageDto> history;


}
