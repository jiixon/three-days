package com.itsu.threedays.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

//@Slf4j
//@RequestMapping("api")
//@Controller
//public class UserController {
//    @GetMapping("/user")
//    public String findUser(@AuthenticationPrincipal OAuth2User oAuth2User){
//        String email = oAuth2User.getAttribute("email").toString();
//        log.info("email : {}",email);
//
//        return email;
//    }
//
//}
