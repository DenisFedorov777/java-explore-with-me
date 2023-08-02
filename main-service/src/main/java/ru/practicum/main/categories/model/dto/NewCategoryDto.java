package ru.practicum.main.categories.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {

    @Size(min = 1, max = 50, message = "Название от 1 до 50 символов.")
    @NotBlank(message = "Без названия нельзя!")
    String name;
}
