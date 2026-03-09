package com.sparta.no1delivery.domain.order.infrastructure;

import com.sparta.no1delivery.domain.order.domain.service.OptionCheck;
import com.sparta.no1delivery.domain.store.domain.Menu;
import com.sparta.no1delivery.domain.store.domain.MenuOption;
import com.sparta.no1delivery.global.presentation.exception.CustomException;
import com.sparta.no1delivery.global.presentation.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionCheckImpl implements OptionCheck {

    @Override
    public void validate(Menu menu, List<String> optionNames) {

        // 옵션이 없는 경우 검증할 필요 없음
        if (optionNames == null || optionNames.isEmpty()) {
            return;
        }

        // 메뉴에 등록된 옵션 목록 가져오기
        List<MenuOption> options = menu.getOptions();

        for (String optionName : optionNames) {

            // 메뉴에 해당 옵션이 존재하는지 확인
            boolean exists = options.stream()
                    .anyMatch(option -> option.getName().equals(optionName));

            // 존재하지 않는 옵션이면 예외 발생
            if (!exists) {
                throw new CustomException(ErrorCode.INVALID_OPTION_DATA);
            }
        }
    }
}