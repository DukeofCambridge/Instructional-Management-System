package com.tan.labbackend.dto;

import com.google.common.base.Converter;
import com.tan.labbackend.entity.Role;
import com.tan.labbackend.entity.User;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import static com.tan.labbackend.utils.BeanUtils.updateProperties;

@Data
@ToString
public class UserDTO {
    @NotNull
    private Integer id;

    private String username;

    private String phone;

    private String email;

    private boolean enabled;

    private String name;

    private Role role;

    public User convertToUser(){
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        User convert = userDTOConvert.convert(this);
        return convert;
    }
    public UserDTO convertFor(User user){
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        UserDTO convert = userDTOConvert.reverse().convert(user);
        return convert;
    }
    private static class UserDTOConvert extends Converter<UserDTO, User> {
        @Override
        @NonNull
        protected User doForward(@NonNull UserDTO userDTO) {
            User user = new User();
            updateProperties(userDTO, user);
            return user;
        }

        @Override
        @NonNull
        protected UserDTO doBackward(@NonNull User user) {
            UserDTO userDTO = new UserDTO();
            updateProperties(user, userDTO);
            return userDTO;
        }
    }

}
