package com.itsu.threedays.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String nickname;
    String email;
    String kakaoAccessToken;
    //String firebaseToken;

}
