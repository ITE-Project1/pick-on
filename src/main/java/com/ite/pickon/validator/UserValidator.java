package com.ite.pickon.validator;

import com.ite.pickon.domain.user.dto.UserVO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserVO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserVO userVO = (UserVO) o;

        // 사용자 이름 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (userVO.getUsername().length() < 3 || userVO.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username", "사용자 이름은 3자 이상 32자 이하이어야 합니다.");
        }

        // 비밀번호 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (userVO.getPassword().length() < 8 || userVO.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "비밀번호는 8자 이상 32자 이하이어야 합니다.");
        }

        // 전화번호 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone_number", "NotEmpty");
        if (!userVO.getPhone_number().matches("010-\\d{4}-\\d{4}")) {
            errors.rejectValue("phone_number", "Pattern.userForm.phoneNumber", "유효하지 않은 전화번호 형식입니다.");
        }
    }
}
