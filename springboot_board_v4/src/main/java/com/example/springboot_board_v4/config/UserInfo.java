package com.example.springboot_board_v4.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.springboot_board_v4.model.member.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class UserInfo implements UserDetails {

    private Member member;

    public UserInfo(Member member) {
        log.info("member 생성 됨");
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 리턴
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                
                return member.getRole().name();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        // 사용자의 패스워드를 리턴
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        // 사용자의 아이디를 리턴
        return member.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 리턴
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 리턴
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 접속 권한 만료 여부 리턴
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 사용 가능 여부 리턴
        return true;
    }
    
}
