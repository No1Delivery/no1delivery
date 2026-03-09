package com.sparta.no1delivery.global.domain;

import java.util.List;

public interface RoleCheck {
    boolean hasRole(String role);
    boolean hasRole(List<String> roles);
}
