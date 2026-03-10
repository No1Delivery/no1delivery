package com.sparta.no1delivery.domain.user.domain.service;

import com.sparta.no1delivery.domain.user.domain.vo.Token;

public interface TokenGenerator {
    Token generate(String loginId);
}
