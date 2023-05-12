package com.itsu.threedays.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request Header에서 토큰 추출
        String jwt = jwtTokenProvider.resolveToken(request);
        log.info("jwt filter!");
        log.info("jwt: {}",jwt);

        //Token 유효성 검사
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){

            //토큰 인증받은 유저인 UsernamePasswordAuthenticiationToken을 리턴
            Authentication auth = jwtTokenProvider.getAuthentication(jwt);
            log.info("authentication!");

            SecurityContextHolder.getContext().setAuthentication(auth); //토큰이 유효한 유저임 -> SecurityContext에 저장
        }

        filterChain.doFilter(request,response);
    }

//    private String resolveToken(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) { //authorization이 Bearer 인지 확인
//            return bearerToken.substring(7);
//        }
//        return null;
//    }


}
