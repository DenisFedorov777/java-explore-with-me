package ru.practicum.main.users.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.users.model.User;
import ru.practicum.main.users.model.dto.UserDto;
import ru.practicum.main.users.model.dto.UserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toDtoUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
