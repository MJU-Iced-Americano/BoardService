package com.mju.board.presentation.dto.qnadto;

import lombok.Data;

@Data
public class UserInfoDto {

    private String id;
    private String username;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String gender;
    private String userInformationType;
    private String birth;
    private String profileImageUrl;
}
