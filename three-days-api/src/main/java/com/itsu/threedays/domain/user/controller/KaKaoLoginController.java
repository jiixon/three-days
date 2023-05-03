package com.itsu.threedays.domain.user.controller;

import com.itsu.threedays.config.jwt.JwtTokenProvider;
import com.itsu.threedays.domain.user.dto.UserDto;
import com.itsu.threedays.domain.user.entity.UserEntity;
import com.itsu.threedays.domain.user.entity.role.Role;
import com.itsu.threedays.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
@Controller
public class KaKaoLoginController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    ResponseEntity<String> responseJwtToken(@RequestBody UserDto userDto) { //파베 토큰, 엑세스 토큰, 디바이스 아디 받아옴
        String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + userDto.getKakaoAccessToken());
        //Bearer 이후 한칸 띄기(kakao) +  accesstoken 헤더에 넣어야함

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);

        RestTemplate restTemplate = new RestTemplate(); //서버에서 다른서버로 연결할 때 쓰는 RestTemplate
        try {
            ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
            //카카오에서 받은 거를 response로 담김
            log.info("response : {}", response);
            log.info("response.getBody() : {}", response.getBody());

            JSONObject jsonObject = new JSONObject(response.getBody());
            JSONObject kakao_account = jsonObject.getJSONObject("kakao_account");//kakao-account : 키값

            String email = kakao_account.getString("email");
            log.info("email : {}", email);
            String nickname = kakao_account.getJSONObject("profile").getString("nickname");
            log.info("nickname : {}", nickname);

            //security config - password는 암호화해서 저장

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(Role.USER.toString());
            //if (userService.findByEmail(email).toString().isEmpty()) {
            if (!userService.isUserExist(email)){
                log.info("회원가입 시작");
                UserEntity user = UserEntity.builder()
                        .role(Role.USER)
                        .id(userDto.getId())
                        .email(email)
                        .nickname(nickname)
                        .password(new BCryptPasswordEncoder().encode(email))
//                        .fireBaseToken(userDto.getFirebaseToken())
                        .build();
                userService.saveUser(user);

                log.info("user: {}",user);


                User createUser = new User(email, "", Collections.singleton(simpleGrantedAuthority));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(createUser, userDto.getKakaoAccessToken(), Collections.singleton(simpleGrantedAuthority));
                //String token = tokenProvider.createToken(usernamePasswordAuthenticationToken);
                //log.info("token 발급: {}", token);
                //return new ResponseEntity<>(token, HttpStatus.OK);

            } else {
                log.info("이미 등록된 회원");
//                if (!userService.getUserId(email).get().getFireBaseToken().equals(userDto.getFirebaseToken())) { //파이어베이스 토큰 다르면 업데이트
//                    log.info("firebaseToken Update");
//                    User userId = userService.getUserId(email).get();
////                    userId.setFireBaseToken(userDto.getFirebaseToken());
//                    userService.saveUser(userId);
//                }

//                User createUser = new User(email, "", Collections.singleton(simpleGrantedAuthority));
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(createUser, userDto.getKakaoAccessToken(), Collections.singleton(simpleGrantedAuthority));
//                String token = tokenProvider.createToken(usernamePasswordAuthenticationToken);
//                log.info("token 발급: {}", token);
//                return new ResponseEntity<>(token, HttpStatus.OK);
            }
        } catch (HttpClientErrorException e) {
            log.error("access token err : {}", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
