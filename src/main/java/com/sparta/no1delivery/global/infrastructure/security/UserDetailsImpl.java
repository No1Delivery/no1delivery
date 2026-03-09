package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 인가
        return user == null ?
                    List.of() :
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user == null ? null : user.getPassword();
    }

    @Override
    public String getUsername() {
        return user == null ? null : user.getLoginId();
    }

    public Long getUserId() {
        return user == null ? null : user.getUserId();
    }

    public String getName() {
        return user == null ? null : user.getNickname();
    }

    /**
     * 계정이 만료가 되지 않았는지 / @ExceptionHandler
     *  false : AccountExpiredException
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨 있는 않느냐?
     * false : LockedException
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * 비번이 만료가 되지 않았느냐?
     * false - CredentialsExpiredException
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 회원이 이용가능한가?(탈퇴한 회원인지 아닌지를 구분할때)
     * - false : 탈퇴한 회원 - DisabledException
     * @return
     */
    @Override
    public boolean isEnabled() {
        return user != null && user.getDeletedAt() == null;
    }
}
