package com.itsu.threedays.config;

import com.itsu.threedays.config.jwt.JwtFilter;
import com.itsu.threedays.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests().mvcMatchers("/api/login").permitAll()
                .mvcMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    //add  : User~Filter전에 jwt(만든)filter 먼저 거친다(jwtFiler만들어야함)

    //.authorizeRequests() : 시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미
    //.mvcMatchers("") : ""(특정경로)로 지정해서 권한 설정 가능
    //.permitAll() : 앞의 것들로 설정한 리소스의 접근을 인증절차 없이 허용한다

    //.anyRequest().authenticated() : 그 외 모든 요청에 대해 인증 필요

    //.csrf().disable() : CSRF 보호 기능 비활성화
    //permitAll -> 로그인할때만
    //나머지는 role(USER)일때 만

}
