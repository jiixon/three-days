package com.itsu.threedays.domain.user.service;


import com.itsu.threedays.domain.user.entity.UserEntity;
import com.itsu.threedays.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findByEmail(String email) {
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        return byEmail.get();
    }
    public boolean isUserExist(String email){
        try {
            findByEmail(email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveUser(UserEntity user){
       userRepository.save(user);
    }

//    public Optional getUserId(String email){
//        userRepository.
//    }





}
