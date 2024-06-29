package com.ite.pickon.security;

package com.ite.pickon.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이 예제에서는 사용자를 하드코딩하여 반환
        // 실제로는 데이터베이스에서 사용자를 조회해야 함
        if ("yjpark".equals(username)) {
            return new User("yjpark", "$2a$10$DowJones/Q32FAR2nY0mDf.eaxjZ0s1nY48D68E1U7G6dXxAopN2lC", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
