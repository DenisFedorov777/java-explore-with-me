package ru.practicum.main.users.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;
    @NotBlank
    @Size(min = 2, max = 250)
    String name;
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    String email;
}
