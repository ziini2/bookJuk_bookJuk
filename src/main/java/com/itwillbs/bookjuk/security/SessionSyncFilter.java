package com.itwillbs.bookjuk.security;

import com.itwillbs.bookjuk.entity.UserEntity;
import com.itwillbs.bookjuk.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionSyncFilter  extends OncePerRequestFilter {
    //Httpsession 과 시큐리티 세션 동기화 가 필요하다!
    //세션에는 값이 있지만 시큐리티 세션에 값이 초기화 되어버리면 세션값이 동기화가 되지 않아서 오류가 발생한다

    private final UserRepository userRepository;

    public SessionSyncFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //세션이 존재할 때만 가져온다
        HttpSession session = request.getSession(false);

        if (session != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //세션에서 유저 정보를 가져온다
            UserEntity user = userRepository.findByUserNum((Long) session.getAttribute("userNum"));

            if (user != null){
                CustomUserDetails userDetails = new CustomUserDetails(user);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
