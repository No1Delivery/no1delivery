package com.sparta.no1delivery.global.infrastructure.security;

import com.sparta.no1delivery.domain.user.domain.entity.User;
import com.sparta.no1delivery.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserDetailsImpl(user);
    }
}
