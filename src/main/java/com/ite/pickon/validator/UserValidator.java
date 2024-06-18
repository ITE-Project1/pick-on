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
            errors.rejectValue("username", "Size.userForm.username", "Username must be between 3 and 32 characters");
        }

        // 비밀번호 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (userVO.getPassword().length() < 8 || userVO.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password must be between 8 and 32 characters");
        }

        // 전화번호 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone_number", "NotEmpty");
        if (!userVO.getPhone_number().matches("010-\\d{4}-\\d{4}")) {
            errors.rejectValue("phone_number", "Pattern.userForm.phoneNumber", "Invalid phone number format");
        }
    }
}
